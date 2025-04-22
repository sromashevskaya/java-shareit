package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto addBooking(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam("approved") @NotNull Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findBookingByUserId(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.findBookingByUserId(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> findAllBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) Long ownerId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerId(ownerId, state);
    }
}