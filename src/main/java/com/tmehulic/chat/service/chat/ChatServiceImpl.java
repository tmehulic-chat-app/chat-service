package com.tmehulic.chat.service.chat;

import com.tmehulic.chat.model.ChatRoom;
import com.tmehulic.chat.model.Message;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private List<ChatRoom> ROOM_LIST;

    public ChatServiceImpl() {
        init();
    }

    private void init() {
        ChatRoom room1 =
                ChatRoom.builder()
                        .name("test")
                        .uuid(UUID.randomUUID())
                        .history(new ArrayDeque<>())
                        .messages(Sinks.many().multicast().onBackpressureBuffer())
                        .build();
        ChatRoom room2 =
                ChatRoom.builder()
                        .name("random")
                        .uuid(UUID.randomUUID())
                        .history(new ArrayDeque<>())
                        .messages(Sinks.many().multicast().onBackpressureBuffer())
                        .build();

        ROOM_LIST = List.of(room1, room2);
    }

    public ChatRoom getRoom(String name) {
        return ROOM_LIST.stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    @Override
    public Flux<Message> getMessages(ChatRoom room) {
        return room.getMessages().asFlux();
    }

    @Override
    public Flux<Message> getHistory(ChatRoom room) {
        return Flux.fromIterable(room.getHistory());
    }

    @Override
    public void addMessage(Message message, ChatRoom room) {
        room.getHistory().add(message);
        room.getMessages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public void joined(String user, ChatRoom room) {
        var message = new Message(user, String.format("%s has joined the room!", user));
        room.getMessages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public void left(String user, ChatRoom room) {
        var message = new Message(user, String.format("%s has left the room!", user));
        room.getMessages().emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
