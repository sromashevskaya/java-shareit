package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> itemStorage = new HashMap<>();
    private long id = 1L;

    @Override
    public Item addItem(Item item) {
        item.setId(id++);
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, ItemDto itemDto) {
        Item item = itemStorage.get(itemId);
        if (item == null) {
            throw new NoSuchElementException("Объект не найден " + itemId);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return item;
    }

    @Override
    public List<Item> findAllItemsByUserId(Long userId) {
        return itemStorage.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .toList();
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return Optional.ofNullable(itemStorage.get(itemId));
    }

    @Override
    public Collection<Item> getSearch(String text) {
        return itemStorage.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }
}
