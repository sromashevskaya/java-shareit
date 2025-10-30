package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> findAllItemRequestsByRequestorId(Long userId);

    List<ItemRequestResponseDto> findAllItemRequestsExceptUserId(Long userId);

    ItemRequestResponseDto findItemRequestById(Long requestId, Long userId);
}