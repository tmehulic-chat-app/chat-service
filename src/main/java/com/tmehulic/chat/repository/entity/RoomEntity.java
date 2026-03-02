package com.tmehulic.chat.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
public class RoomEntity {

    @Id private UUID id;

    @Column(value = "name")
    private String name;

    @Column(value = "description")
    private String description;

    @Version private Long version;
}
