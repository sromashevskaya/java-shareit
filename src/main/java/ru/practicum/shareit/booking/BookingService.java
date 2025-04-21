package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(Long userId, BookingDto bookingDto);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto findBookingByUserId(Long bookingId, Long userId);

    List<BookingDto> findAllBookingsByBookerId(Long bookerId, String state);

    List<BookingDto> findAllBookingsByOwnerId(Long ownerId, String state);
}
