package com.tmehulic.chat.mapper;

import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.repository.entity.MessageEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toDto(MessageEntity entity);

    List<Message> toDto(List<MessageEntity> entities);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "roomId", ignore = true)
    @Mapping(target = "id", ignore = true)
    MessageEntity toEntity(Message message);
}
