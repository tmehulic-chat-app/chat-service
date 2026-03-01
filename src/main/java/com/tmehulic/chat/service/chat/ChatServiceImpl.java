package com.tmehulic.chat.service.chat;

import com.tmehulic.chat.mapper.MessageMapper;
import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.repository.MessageRepository;
import com.tmehulic.chat.repository.entity.MessageEntity;
import com.tmehulic.chat.service.room.RoomService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
@Slf4j
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
        return roomService
                .findOne(id)
                .map(room -> new ChatRoom(id, room.name()))
                .doOnNext(chatRoom -> rooms.put(id, chatRoom));
    }

    @Override
    public Flux<Message> getMessages(ChatRoom room) {
        return room.messages().asFlux();
    }

    @Override
    public Flux<Message> getHistory(ChatRoom room) {
        return messageRepository.findByRoomId(room.uuid()).map(messageMapper::toDto);
    }

    @Override
    public Mono<Void> addMessage(Message message, ChatRoom room) {
        MessageEntity entity = messageMapper.toEntity(message);
        entity.setId(UUID.randomUUID());
        entity.setRoomId(room.uuid());
        entity.setTimestamp(OffsetDateTime.now());

        return messageRepository
                .save(entity)
                .doOnNext(
                        saved ->
                                room.messages()
                                        .emitNext(
                                                messageMapper.toDto(saved),
                                                Sinks.EmitFailureHandler.FAIL_FAST))
                .then();
    }

    @Override
    public Mono<Void> joined(String user, ChatRoom room) {
        var message = new Message(user, String.format("%s has joined the room!", user));
        return Mono.fromRunnable(
                () -> room.messages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST));
    }

    @Override
    public Mono<Void> left(String user, ChatRoom room) {
        var message = new Message(user, String.format("%s has left the room!", user));
        return Mono.fromRunnable(
                () -> room.messages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST));
    }
}
