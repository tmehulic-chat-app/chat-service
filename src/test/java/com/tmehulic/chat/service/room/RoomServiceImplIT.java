// package com.tmehulic.chat.service.room;
//
// import com.tmehulic.chat.model.Room;
// import com.tmehulic.chat.model.RoomRequest;
//
// import jakarta.validation.ConstraintViolationException;
//
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
//
// import java.util.List;
// import java.util.UUID;
//
// @ActiveProfiles({"test", "test-override"})
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// class RoomServiceImplIT {
//    @Autowired private RoomService roomService;
//
//    @Test
//    void shouldCreateRoom() {
//        RoomRequest request = new RoomRequest();
//        request.setName("Integration Test Room");
//        request.setDescription("Room for integration testing");
//        Room created = roomService.create(request);
//        Assertions.assertNotNull(created);
//        Assertions.assertNotNull(created.id());
//        Assertions.assertEquals(request.getName(), created.name());
//        Assertions.assertEquals(request.getDescription(), created.description());
//    }
//
//    @Test
//    void shouldFindRoomById() {
//        RoomRequest request = new RoomRequest();
//        request.setName("Find Room");
//        request.setDescription("Find by id");
//        Room created = roomService.create(request);
//        Room found = roomService.findOne(created.id());
//        Assertions.assertNotNull(found);
//        Assertions.assertEquals(created.id(), found.id());
//        Assertions.assertEquals("Find Room", found.name());
//    }
//
//    @Test
//    void shouldUpdateRoom() {
//        RoomRequest request = new RoomRequest();
//        request.setName("Update Room");
//        request.setDescription("Before update");
//        Room created = roomService.create(request);
//        RoomRequest updateRequest = new RoomRequest();
//        updateRequest.setName("Updated Room");
//        updateRequest.setDescription("After update");
//        updateRequest.setId(created.id());
//        Room updated = roomService.update(updateRequest);
//        Assertions.assertEquals(created.id(), updated.id());
//        Assertions.assertEquals("Updated Room", updated.name());
//        Assertions.assertEquals("After update", updated.description());
//    }
//
//    @Test
//    void shouldDeleteRoom() {
//        RoomRequest request = new RoomRequest();
//        request.setName("Delete Room");
//        request.setDescription("To be deleted");
//        Room created = roomService.create(request);
//        UUID id = created.id();
//        roomService.delete(id);
//        IllegalArgumentException thrown =
//                Assertions.assertThrows(
//                        IllegalArgumentException.class, () -> roomService.findOne(id));
//        Assertions.assertTrue(thrown.getMessage().contains("Room not found"));
//    }
//
//    @Test
//    void shouldFindAllRooms() {
//        RoomRequest request1 = new RoomRequest();
//        request1.setName("Room 1");
//        request1.setDescription("First room");
//        RoomRequest request2 = new RoomRequest();
//        request2.setName("Room 2");
//        request2.setDescription("Second room");
//        roomService.create(request1);
//        roomService.create(request2);
//        List<Room> rooms = roomService.find();
//        boolean foundRoom1 = rooms.stream().anyMatch(r -> "Room 1".equals(r.name()));
//        boolean foundRoom2 = rooms.stream().anyMatch(r -> "Room 2".equals(r.name()));
//        Assertions.assertTrue(foundRoom1, "Room 1 should be present in the list");
//        Assertions.assertTrue(foundRoom2, "Room 2 should be present in the list");
//    }
//
//    @Test
//    void shouldThrowValidationExceptionForNullName() {
//        RoomRequest request = new RoomRequest();
//        request.setName(null);
//        request.setDescription("desc");
//        Assertions.assertThrows(
//                ConstraintViolationException.class, () -> roomService.create(request));
//    }
//
//    @Test
//    void shouldThrowValidationExceptionForShortName() {
//        RoomRequest request = new RoomRequest();
//        request.setName("ab"); // too short
//        request.setDescription("desc");
//        Assertions.assertThrows(
//                ConstraintViolationException.class, () -> roomService.create(request));
//    }
//
//    @Test
//    void shouldThrowValidationExceptionForLongDescription() {
//        RoomRequest request = new RoomRequest();
//        request.setName("Valid Name");
//        request.setDescription("a".repeat(256)); // too long
//        Assertions.assertThrows(
//                ConstraintViolationException.class, () -> roomService.create(request));
//    }
//
//    @Test
//    void shouldThrowWhenFindingNonExistentRoom() {
//        UUID randomId = UUID.randomUUID();
//        Assertions.assertThrows(
//                IllegalArgumentException.class, () -> roomService.findOne(randomId));
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingNonExistentRoom() {
//        RoomRequest request = new RoomRequest();
//        request.setId(UUID.randomUUID());
//        request.setName("Update");
//        request.setDescription("desc");
//        Assertions.assertThrows(IllegalArgumentException.class, () ->
// roomService.update(request));
//    }
//
//    @Test
//    void shouldNotThrowWhenDeletingNonExistentRoom() {
//        UUID randomId = UUID.randomUUID();
//        Assertions.assertDoesNotThrow(() -> roomService.delete(randomId));
//    }
// }
