package com.tmehulic.chat.service.room;

import com.tmehulic.chat.mapper.RoomMapper;
import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;
import com.tmehulic.chat.repository.RoomRepository;
import com.tmehulic.chat.repository.entity.RoomEntity;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public List<Room> find() {
        var rooms = roomRepository.findAll();
        return roomMapper.toDto(rooms);
    }

    @Override
    public Room findOne(UUID id) {
        var room = findByIdOrThrow(id);
        return roomMapper.toDto(room);
    }

    @Override
    public Room create(RoomRequest request) {
        RoomEntity roomEntity = roomMapper.toEntity(request);
        roomEntity = roomRepository.save(roomEntity);
        return roomMapper.toDto(roomEntity);
    }

    @Override
    public Room update(RoomRequest request) {
        RoomEntity roomEntity = findByIdOrThrow(request.getId());
        roomMapper.updateEntity(request, roomEntity);
        roomRepository.save(roomEntity);
        return roomMapper.toDto(roomEntity);
    }

    @Override
    public void delete(UUID id) {
        roomRepository.deleteById(id);
    }

    private RoomEntity findByIdOrThrow(UUID id) {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
    }
}
