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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomServiceImpl roomService;

    @GetMapping(version = "1.0")
    public Flux<Room> find() {
        return roomService.find();
    }

    @GetMapping(path = "/{id}", version = "1.0")
    public Mono<Room> findOne(@PathVariable UUID id) {
        return roomService.findOne(id);
    }

    @PostMapping(version = "1.0")
    public Mono<Room> create(@RequestBody RoomRequest request) {
        return roomService.create(request);
    }

    @PutMapping(path = "/{id}", version = "1.0")
    public Mono<Room> update(@PathVariable UUID id, @RequestBody RoomRequest request) {
        return roomService.update(id, request);
    }

    @DeleteMapping(path = "/{id}", version = "1.0")
    public Mono<Void> delete(@PathVariable UUID id) {
        return roomService.delete(id);
    }
}
