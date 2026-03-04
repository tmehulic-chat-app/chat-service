package com.tmehulic.chat.model;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    public String username() {
        return username.toLowerCase();
    }
}
