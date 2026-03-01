package com.tmehulic.chat.service.room;

import com.tmehulic.chat.mapper.RoomMapper;
import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;
import com.tmehulic.chat.repository.RoomRepository;
import com.tmehulic.chat.repository.entity.RoomEntity;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public Flux<Room> find() {
        return roomRepository.findAll().map(roomMapper::toDto);
    }

    @Override
    public Mono<Room> findOne(UUID id) {
        return findByIdOrThrow(id).map(roomMapper::toDto);
    }

    @Override
    public Mono<Room> create(RoomRequest request) {
        RoomEntity entity = roomMapper.toEntity(request);
        entity.setId(UUID.randomUUID());
        return roomRepository.save(entity).map(roomMapper::toDto);
    }

    @Override
    public Mono<Room> update(UUID id, RoomRequest request) {
        return findByIdOrThrow(id)
                .doOnNext(entity -> roomMapper.updateEntity(request, entity))
                .flatMap(roomRepository::save)
                .map(roomMapper::toDto);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return roomRepository.deleteById(id);
    }

    private Mono<RoomEntity> findByIdOrThrow(UUID id) {
        return roomRepository
                .findById(id)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Room not found with id: " + id)));
    }
}
