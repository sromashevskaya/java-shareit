package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestResponseDto addItemRequest(@RequestHeader(USER_ID_HEADER) Long userId,
                                                    @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> findAllItemRequestsByRequestorId(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.findAllItemRequestsByRequestorId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAllItemRequestsExceptUserId(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.findAllItemRequestsExceptUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findItemRequestById(@PathVariable("requestId") Long requestId,
                                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.findItemRequestById(requestId, userId);
    }
}

/*

 */