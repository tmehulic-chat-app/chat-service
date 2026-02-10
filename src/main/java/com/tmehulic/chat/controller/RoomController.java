package com.tmehulic.chat.controller;

import com.tmehulic.chat.model.Room;
import com.tmehulic.chat.model.RoomRequest;
import com.tmehulic.chat.service.room.RoomServiceImpl;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomServiceImpl roomService;

    @GetMapping(version = "1.0")
    public List<Room> find() {
        return roomService.find();
    }

    @GetMapping(path = "/{uuid}", version = "1.0")
    public Room findOne(@PathVariable UUID uuid) {
        return roomService.findOne(uuid);
    }

    @PostMapping(version = "1.0")
    public Room create(@RequestBody RoomRequest request) {
        return roomService.create(request);
    }

    @PutMapping(path = "/{uuid}", version = "1.0")
    public Room update(@PathVariable UUID uuid, @RequestBody RoomRequest request) {
        request.setId(uuid);
        return roomService.update(request);
    }

    @DeleteMapping(path = "/{uuid}", version = "1.0")
    public void delete(@PathVariable UUID uuid) {
        roomService.delete(uuid);
    }
}
