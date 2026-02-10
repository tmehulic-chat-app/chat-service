package com.tmehulic.chat.service.room;

import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;

import jakarta.validation.Valid;

import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing chat rooms.
 *
 * <p>Provides methods for creating, retrieving, updating, and deleting chat rooms.
 */
@Validated
public interface RoomService {

    /**
     * Retrieves all chat rooms.
     *
     * @return a list of all {@link Room} objects in the system; never null
     */
    List<Room> find();

    /**
     * Finds a chat room by its unique identifier.
     *
     * @param id the {@link UUID} of the room to retrieve
     * @return the {@link Room} object with the specified id, if found; otherwise, throws an
     *     exception
     */
    Room findOne(UUID id);

    /**
     * Creates a new chat room based on the provided request data.
     *
     * @param room the {@link RoomRequest} containing details for the new room
     * @return the created {@link Room} object
     */
    Room create(@Valid RoomRequest room);

    /**
     * Updates an existing chat room with new data.
     *
     * @param room the {@link RoomRequest} containing updated room data
     * @return the updated {@link Room} object, or throws an exception if the room does not exist
     */
    Room update(@Valid RoomRequest room);

    /**
     * Deletes a chat room by its unique identifier.
     *
     * @param id the {@link UUID} of the room to delete
     */
    void delete(UUID id);
}
