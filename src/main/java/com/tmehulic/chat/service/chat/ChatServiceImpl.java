package com.tmehulic.chat.service.chat;

import com.tmehulic.chat.model.ChatRoom;
import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.service.room.RoomService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private RoomService roomService;
    private static final Map<UUID, ChatRoom> rooms = new ConcurrentHashMap<>();

    @Override
    public ChatRoom getRoom(UUID id) {
        if (rooms.containsKey(id)) {
            return rooms.get(id);
        }
        Room room = roomService.findOne(id);
        ChatRoom chatRoom =
                ChatRoom.builder()
                        .name(room.name())
                        .uuid(id)
                        .history(new ArrayDeque<>())
                        .messages(Sinks.many().multicast().onBackpressureBuffer())
                        .build();
        rooms.put(id, chatRoom);
        return chatRoom;
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
