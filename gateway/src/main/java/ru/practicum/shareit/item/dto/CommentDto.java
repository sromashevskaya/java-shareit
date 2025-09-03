package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
<<<<<<<< HEAD:gateway/src/main/java/ru/practicum/shareit/item/dto/CommentDto.java
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private String author;
    private LocalDateTime created;
========
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private Long request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentResponseDto> comments;

    public ItemDto(Long id, String name, String description, Boolean available, Long ownerId, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = ownerId;
        this.request = requestId;
    }
>>>>>>>> origin/main:src/main/java/ru/practicum/shareit/item/dto/ItemDto.java
}