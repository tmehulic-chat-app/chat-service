package com.tmehulic.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import reactor.core.publisher.Sinks;

import java.util.Deque;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ChatRoom {
    private String name;
    private UUID uuid;
    private Sinks.Many<Message> messages;
    private Deque<Message> history;
}
