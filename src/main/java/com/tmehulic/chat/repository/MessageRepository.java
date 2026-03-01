package com.tmehulic.chat.repository;

import com.tmehulic.chat.repository.entity.MessageEntity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, UUID> {

    Flux<MessageEntity> findByRoomId(UUID roomId);
}
