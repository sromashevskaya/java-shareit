package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userid, Long itemId, ItemDto itemDto);

    List<ItemDto> findAllItemsByUserId(Long userId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> getSearch(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    List<CommentDto> findCommentsByItemId(Long itemId);
}
