package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    Long id;
    @NotBlank
    @FutureOrPresent
    LocalDateTime start;
    @NotBlank
    @FutureOrPresent
    LocalDateTime end;
    @NotBlank
    Long itemId;
    Long bookerId;
    @NotBlank
    Status status;
}
