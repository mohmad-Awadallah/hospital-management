package com.hospital.service.impl;

import com.hospital.model.Room;
import com.hospital.repository.RoomRepository;
import com.hospital.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Room createRoom(Room room) {
        logger.debug("إنشاء غرفة جديدة: {}", room);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        logger.debug("جلب جميع الغرف");
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        logger.debug("جلب الغرفة بواسطة ID: {}", id);
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الغرفة غير موجودة"));
    }

    @Override
    public Room updateRoom(Long id, Room room) {
        logger.debug("تحديث الغرفة: {}", room);
        Room existingRoom = getRoomById(id);
        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setRoomType(room.getRoomType());
        existingRoom.setRoomStatus(room.getRoomStatus());
        return roomRepository.save(existingRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        logger.debug("حذف الغرفة بواسطة ID: {}", id);
        roomRepository.deleteById(id);
    }
}