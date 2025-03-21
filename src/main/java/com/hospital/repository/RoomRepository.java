package com.hospital.repository;

import com.hospital.model.Room;
import com.hospital.model.RoomStatus;
import com.hospital.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


}
