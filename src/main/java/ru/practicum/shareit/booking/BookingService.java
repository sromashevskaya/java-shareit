package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(Long userId, BookingDto bookingDto);

    BookingResponseDto updateBooking(Long bookingId, Long userId, Boolean approved);

    BookingResponseDto findBookingByUserId(Long bookingId, Long userId);

    List<BookingResponseDto> findAllBookingsByBookerId(Long bookerId, String state);

    List<BookingResponseDto> findAllBookingsByOwnerId(Long ownerId, String state);
}