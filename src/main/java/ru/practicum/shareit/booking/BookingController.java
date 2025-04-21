package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addBooking(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PutMapping("/{bookingId}")
    public BookingDto updateBooking(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam("approved") @NotNull Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingByUserId(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.findBookingByUserId(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) Long ownerId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerId(ownerId, state);
    }
}