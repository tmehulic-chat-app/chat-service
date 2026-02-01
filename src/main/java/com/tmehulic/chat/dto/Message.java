package com.tmehulic.chat.dto;

import java.time.OffsetDateTime;

public record Message(String author, String content, OffsetDateTime created) {
    public Message(String author, String content) {
        this(author, content, OffsetDateTime.now());
    }
}
