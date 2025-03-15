package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "رقم الغرفة مطلوب")
    @Column(name = "room_number")
    private String roomNumber;

    @NotNull(message = "نوع الغرفة مطلوب")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @NotNull(message = "حالة الغرفة مطلوبة")
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Admission> admissions = new ArrayList<>();
}