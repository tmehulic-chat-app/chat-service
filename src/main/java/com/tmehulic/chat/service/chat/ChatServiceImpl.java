package com.tmehulic.chat.service.chat;

import com.tmehulic.chat.mapper.MessageMapper;
import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.repository.MessageRepository;
import com.tmehulic.chat.repository.entity.MessageEntity;
import com.tmehulic.chat.service.room.RoomService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private RoomService roomService;
    private MessageRepository messageRepository;
    private MessageMapper messageMapper;

    private static final Map<UUID, ChatRoom> rooms = new ConcurrentHashMap<>();

    @Override
    public Mono<ChatRoom> getRoom(UUID id) {
        ChatRoom existing = rooms.get(id);
        if (existing != null) {
            return Mono.just(existing);
        }
        return Mono.fromCallable(() -> roomService.findOne(id))
                .subscribeOn(Schedulers.boundedElastic())
                .map(room -> new ChatRoom(id, room.name()))
                .doOnNext(chatRoom -> rooms.putIfAbsent(id, chatRoom));
    }

    @Override
    public Flux<Message> getMessages(ChatRoom room) {
        return room.messages().asFlux();
    }

    @Override
    public Flux<Message> getHistory(ChatRoom room) {
        return Mono.fromCallable(() -> messageRepository.findByRoomId(room.uuid()))
                .subscribeOn(Schedulers.boundedElastic())
                .map(entities -> messageMapper.toDto(entities))
                .flatMapIterable(messages -> messages);
    }

    @Override
    public void addMessage(Message message, ChatRoom room) {
        Mono.fromCallable(
                        () -> {
                            MessageEntity entity = messageMapper.toEntity(message);
                            entity.setRoomId(room.uuid());
                            messageRepository.save(entity);
                            return entity;
                        })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(
                        entity -> {
                            room.messages()
                                    .emitNext(
                                            messageMapper.toDto(entity),
                                            Sinks.EmitFailureHandler.FAIL_FAST);
                        })
                .subscribe();
    }

    @Override
    public void joined(String user, ChatRoom room) {
        var message = new Message(user, String.format("%s has joined the room!", user));
        room.messages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public void left(String user, ChatRoom room) {
        var message = new Message(user, String.format("%s has left the room!", user));
        room.messages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
