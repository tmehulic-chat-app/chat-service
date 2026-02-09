package com.tmehulic.chat.repository;

import com.tmehulic.chat.repository.entity.RoomEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {}
