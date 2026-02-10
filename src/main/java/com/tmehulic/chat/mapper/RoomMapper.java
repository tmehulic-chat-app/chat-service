package com.tmehulic.chat.mapper;

import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;
import com.tmehulic.chat.repository.entity.RoomEntity;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomEntity toEntity(RoomRequest request);

    void updateEntity(RoomRequest request, @MappingTarget RoomEntity roomEntity);

    Room toDto(RoomEntity roomEntity);

    List<Room> toDto(List<RoomEntity> roomEntities);
}
