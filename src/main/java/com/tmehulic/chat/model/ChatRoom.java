package com.tmehulic.chat.model;

import reactor.core.publisher.Sinks;

import java.util.UUID;

public record ChatRoom(String name, UUID uuid, Sinks.Many<Message> messages) {

    public ChatRoom(UUID uuid, String name) {
        this(name, uuid, Sinks.many().multicast().onBackpressureBuffer());
    }
}
