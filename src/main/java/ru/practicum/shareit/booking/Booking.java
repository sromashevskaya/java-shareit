package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date_time")
    private LocalDateTime start;
    @Column(name = "end_date_time")
    private LocalDateTime end;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Long item;
    @OneToOne
    @JoinColumn(name = "booker_id")
    private Long booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private Status status;
}
