package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByBookerIdAndInTimePeriod(Long bookerId, LocalDateTime start,
                                                LocalDateTime end);

    List<Booking> findByBookerIdBeforeEnd(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAfterStart(Long bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status);

    List<Booking> findByOwnerId(Long ownerId);

    List<Booking> findByOwnerIdAndInTimePeriod(Long ownerId, LocalDateTime start,
                                               LocalDateTime end);

    List<Booking> findByOwnerIdBeforeEnd(Long bookerId, LocalDateTime end);

    List<Booking> findByOwnerIdAfterStart(Long bookerId, LocalDateTime start);

    List<Booking> findByOwnerIdAndStatus(Long bookerId, Status status);

    List<Booking> findByBooker_IdAndItem_IdAndEndIsBeforeAndStatus(Long userId, Long itemId,
                                                                   LocalDateTime end, Status status);

}