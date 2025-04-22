package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> findAllItemsByUserId(Long userId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> getSearch(String text);

    CommentResponseDto addComment(Long userId, Long itemId, CommentDto commentDto);

    List<CommentResponseDto> findCommentsByItemId(Long itemId);
}