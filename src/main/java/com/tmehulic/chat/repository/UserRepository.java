package com.tmehulic.chat.repository;

import com.tmehulic.chat.repository.entity.UserEntity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {

    Mono<UserEntity> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);
}
