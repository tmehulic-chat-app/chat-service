package com.tmehulic.chat.service.chat;

import com.tmehulic.chat.mapper.MessageMapper;
import com.tmehulic.chat.model.ChatRoom;
import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.model.Room;
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
    public ChatRoom getRoom(UUID id) {
        if (rooms.containsKey(id)) {
            return rooms.get(id);
        }
        Room room = roomService.findOne(id);
        ChatRoom chatRoom = new ChatRoom(id, room.name());

        rooms.put(id, chatRoom);
        return chatRoom;
    }

    @Override
    public Flux<Message> getMessages(ChatRoom room) {
        return room.messages().asFlux();
    }

    @Override
    public Flux<Message> getHistory(ChatRoom room) {
        return Mono.fromCallable(
                        () -> {
                            return messageRepository.findByRoomId(room.uuid());
                        })
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
                            room.messages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
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
