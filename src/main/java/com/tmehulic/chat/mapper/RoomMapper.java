package com.tmehulic.chat.mapper;

import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;
import com.tmehulic.chat.repository.entity.RoomEntity;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    RoomEntity toEntity(RoomRequest request);

    @InheritConfiguration
    void updateEntity(RoomRequest request, @MappingTarget RoomEntity roomEntity);

    Room toDto(RoomEntity roomEntity);

    List<Room> toDto(List<RoomEntity> roomEntities);
}
