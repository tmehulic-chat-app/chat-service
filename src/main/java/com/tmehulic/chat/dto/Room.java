package com.tmehulic.chat.dto;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record Room(@Nullable UUID id, @NonNull String name, @Nullable String description) {}
