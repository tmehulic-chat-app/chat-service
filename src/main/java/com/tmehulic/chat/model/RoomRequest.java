package com.tmehulic.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
public class RoomRequest implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    @JsonIgnore private UUID id;

    @NotBlank
    @Size(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
    private String name;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
}
