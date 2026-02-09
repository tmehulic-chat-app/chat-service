package com.tmehulic.chat.service;

import com.tmehulic.chat.dto.Room;
import com.tmehulic.chat.mapper.RoomMapper;
import com.tmehulic.chat.repository.RoomRepository;
import com.tmehulic.chat.repository.entity.RoomEntity;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<Room> find() {
        var rooms = roomRepository.findAll();
        return roomMapper.toDto(rooms);
    }

    public Room findOne(UUID id) {
        var room = findByIdOrThrow(id);
        return roomMapper.toDto(room);
    }

    public Room create(Room room) {
        RoomEntity roomEntity = roomMapper.toEntity(room);
        roomEntity = roomRepository.save(roomEntity);
        return roomMapper.toDto(roomEntity);
    }

    public Room update(UUID uuid, Room room) {
        RoomEntity roomEntity = findByIdOrThrow(uuid);
        roomMapper.updateEntity(room, roomEntity);
        roomRepository.save(roomEntity);
        return roomMapper.toDto(roomEntity);
    }

    public void delete(UUID id) {
        roomRepository.deleteById(id);
    }

    private RoomEntity findByIdOrThrow(UUID id) {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
    }
}
