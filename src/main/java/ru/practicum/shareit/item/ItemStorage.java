package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item addItem(Item item);

    Item updateItem(Long itemId, ItemDto itemDto);

    List<Item> findAllItemsByUserId(Long userId);

    Optional<Item> findItemById(Long itemId);

    Collection<Item> getSearch(String text);
}