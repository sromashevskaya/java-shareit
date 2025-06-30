package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam("approved") Boolean approved) {
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingByUserId(
            @PathVariable Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.findBookingByUserId(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.findAllBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) Long ownerId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.findAllBookingsByOwnerId(ownerId, state);
    }
}