package com.tmehulic.chat.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    @Id private UUID id;

    @Column private String content;

    @Column private String sender;

    @Column private OffsetDateTime timestamp;

    @Column(value = "room_id")
    private UUID roomId;

    @Version private Long version;
}
