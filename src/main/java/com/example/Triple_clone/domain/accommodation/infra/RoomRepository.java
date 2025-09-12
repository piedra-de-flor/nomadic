package com.example.Triple_clone.domain.accommodation.infra;

import com.example.Triple_clone.domain.accommodation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
