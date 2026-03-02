package com.tmehulic.chat.service.room;

import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;

@ActiveProfiles({"test", "test-override"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RoomServiceImplIT {
    @Autowired private RoomService roomService;

    private RoomRequest buildRequest(String name, String description) {
        RoomRequest request = new RoomRequest();
        request.setName(name);
        request.setDescription(description);
        return request;
    }

    @Test
    void shouldCreateRoom() {
        RoomRequest request = buildRequest("Integration Test Room", "Room for integration testing");

        StepVerifier.create(roomService.create(request))
                .assertNext(
                        created -> {
                            Assertions.assertNotNull(created);
                            Assertions.assertNotNull(created.id());
                            Assertions.assertEquals(request.getName(), created.name());
                            Assertions.assertEquals(
                                    request.getDescription(), created.description());
                        })
                .verifyComplete();
    }

    @Test
    void shouldFindRoomById() {
        StepVerifier.create(
                        roomService
                                .create(buildRequest("Find Room", "Find by id"))
                                .flatMap(
                                        created ->
                                                roomService
                                                        .findOne(created.id())
                                                        .map(found -> Map.entry(created, found))))
                .assertNext(
                        entry -> {
                            Assertions.assertEquals(entry.getKey().id(), entry.getValue().id());
                            Assertions.assertEquals("Find Room", entry.getValue().name());
                        })
                .verifyComplete();
    }

    @Test
    void shouldUpdateRoom() {
        RoomRequest updateRequest = buildRequest("Updated Room", "After update");

        StepVerifier.create(
                        roomService
                                .create(buildRequest("Update Room", "Before update"))
                                .flatMap(
                                        created -> {
                                            return roomService
                                                    .update(created.id(), updateRequest)
                                                    .map(updated -> Map.entry(created, updated));
                                        }))
                .assertNext(
                        entry -> {
                            Assertions.assertEquals(entry.getKey().id(), entry.getValue().id());
                            Assertions.assertEquals("Updated Room", entry.getValue().name());
                            Assertions.assertEquals("After update", entry.getValue().description());
                        })
                .verifyComplete();
    }

    @Test
    void shouldDeleteRoom() {
        StepVerifier.create(
                        roomService
                                .create(buildRequest("Delete Room", "To be deleted"))
                                .flatMap(
                                        created ->
                                                roomService
                                                        .delete(created.id())
                                                        .then(roomService.findOne(created.id()))))
                .expectErrorMatches(
                        ex ->
                                ex instanceof IllegalArgumentException
                                        && ex.getMessage().contains("Room not found"))
                .verify();
    }

    @Test
    void shouldFindAllRooms() {
        StepVerifier.create(
                        roomService
                                .create(buildRequest("Room 1", "First room"))
                                .then(roomService.create(buildRequest("Room 2", "Second room")))
                                .thenMany(roomService.find())
                                .map(Room::name)
                                .collectList())
                .assertNext(
                        names -> {
                            Assertions.assertTrue(names.contains("Room 1"));
                            Assertions.assertTrue(names.contains("Room 2"));
                        })
                .verifyComplete();
    }

    @Test
    void shouldThrowValidationExceptionForNullName() {
        StepVerifier.create(Mono.defer(() -> roomService.create(buildRequest(null, "desc"))))
                .expectError(ConstraintViolationException.class)
                .verify();
    }

    @Test
    void shouldThrowValidationExceptionForShortName() {
        StepVerifier.create(Mono.defer(() -> roomService.create(buildRequest("ab", "desc"))))
                .expectError(ConstraintViolationException.class)
                .verify();
    }

    @Test
    void shouldThrowValidationExceptionForLongDescription() {
        StepVerifier.create(
                        Mono.defer(
                                () ->
                                        roomService.create(
                                                buildRequest("Valid Name", "a".repeat(256)))))
                .expectError(ConstraintViolationException.class)
                .verify();
    }

    @Test
    void shouldThrowWhenFindingNonExistentRoom() {
        StepVerifier.create(roomService.findOne(UUID.randomUUID()))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentRoom() {
        RoomRequest request = buildRequest("Update", "desc");
        UUID randomId = UUID.randomUUID();

        StepVerifier.create(roomService.update(randomId, request))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldNotThrowWhenDeletingNonExistentRoom() {
        StepVerifier.create(roomService.delete(UUID.randomUUID())).verifyComplete();
    }
}
