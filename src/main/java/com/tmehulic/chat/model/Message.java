package com.tmehulic.chat.model;

import java.time.OffsetDateTime;

public record Message(String sender, String content, OffsetDateTime timestamp) {
    public Message(String sender, String content) {
        this(sender, content, OffsetDateTime.now());
    }
}
