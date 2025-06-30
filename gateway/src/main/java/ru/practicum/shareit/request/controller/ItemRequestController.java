package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByUserId(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestClient.findAllItemRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long requestId,
                                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestClient.findItemRequestById(requestId, userId);
    }
}
