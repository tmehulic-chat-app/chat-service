package com.tmehulic.chat.service.room;

import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;

import jakarta.validation.Valid;

import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * @return flux of all {@link Room} objects in the system
     */
    Flux<Room> find();

    /**
     * Finds a chat room by its unique identifier.
     *
     * @param id the {@link UUID} of the room to retrieve
     * @return the {@link Room} object with the specified id, if found; otherwise, throws an
     *     exception
     */
    Mono<Room> findOne(UUID id);

    /**
     * Creates a new chat room based on the provided request data.
     *
     * @param room the {@link RoomRequest} containing details for the new room
     * @return the created {@link Room} object
     */
    Mono<Room> create(@Valid RoomRequest room);

    /**
     * Updates an existing chat room with new data.
     *
     * @param id the {@link UUID} of the room to update
     * @param room the {@link RoomRequest} containing updated room data
     * @return the updated {@link Room} object, or throws an exception if the room does not exist
     */
    Mono<Room> update(UUID id, @Valid RoomRequest room);

    /**
     * Deletes a chat room by its unique identifier.
     *
     * @param id the {@link UUID} of the room to delete
     * @return a Mono of Void
     */
    Mono<Void> delete(UUID id);
}
