package com.tmehulic.chat.service.chat;

import com.tmehulic.chat.model.ChatRoom;
import com.tmehulic.chat.model.Message;

import reactor.core.publisher.Flux;

import java.util.UUID;

/** Service interface for chat room operations, including message handling and user presence. */
public interface ChatService {
    /**
     * Retrieves a chat room by the Room's id.
     *
     * @param id the name of the chat room
     * @return the ChatRoom object
     */
    ChatRoom getRoom(UUID id);

    /**
     * Returns a stream of messages for the given chat room.
     *
     * @param room the chat room
     * @return a Flux of Message objects
     */
    Flux<Message> getMessages(ChatRoom room);

    /**
     * Returns the message history for the given chat room.
     *
     * @param room the chat room
     * @return a Flux of Message objects representing the history
     */
    Flux<Message> getHistory(ChatRoom room);

    /**
     * Adds a new message to the specified chat room.
     *
     * @param message the message to add
     * @param room the chat room
     */
    void addMessage(Message message, ChatRoom room);

    /**
     * Marks a user as having joined the chat room.
     *
     * @param user the username
     * @param room the chat room
     */
    void joined(String user, ChatRoom room);

    /**
     * Marks a user as having left the chat room.
     *
     * @param user the username
     * @param room the chat room
     */
    void left(String user, ChatRoom room);
}
