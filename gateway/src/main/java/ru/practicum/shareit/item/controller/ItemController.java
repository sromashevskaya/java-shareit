package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@NotNull @RequestHeader(USER_ID_HEADER) Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable("itemId") Long itemId,
                                             @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUserId(@NotNull @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.findAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable("itemId") Long itemId) {
        return itemClient.findItemById(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestParam String text) {
        return itemClient.getSearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @PathVariable("itemId") Long itemId,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody CommentDto commentDto) {

        return itemClient.addComment(itemId, userId, commentDto);
    }
}
