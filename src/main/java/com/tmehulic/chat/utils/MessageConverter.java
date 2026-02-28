package com.tmehulic.chat.utils;

import com.tmehulic.chat.model.Message;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class MessageConverter {

    private ObjectMapper objectMapper;

    public Message asMessage(String payload) {
        return objectMapper.readValue(payload, Message.class);
    }

    public String asPayload(Message message) {
        return objectMapper.writeValueAsString(message);
    }
}
