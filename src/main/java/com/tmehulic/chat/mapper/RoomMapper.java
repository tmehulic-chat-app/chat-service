package com.tmehulic.chat.mapper;

import com.tmehulic.chat.dto.Room;
import com.tmehulic.chat.repository.entity.RoomEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomEntity toEntity(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntity(Room room, @MappingTarget RoomEntity roomEntity);

    Room toDto(RoomEntity roomEntity);

    List<Room> toDto(List<RoomEntity> roomEntities);

    List<RoomEntity> toEntity(List<Room> rooms);
}
