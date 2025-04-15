package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = getUserOrThrow(userId);
        itemDto.setOwner(user.getId());
        Item item = itemMapper.toItem(itemDto);
        return itemMapper.toItemDto(itemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item existingItem = getItemOrThrow(itemId);
        if (!existingItem.getOwner().equals(userId)) {
            throw new NotFoundException("Объект не найден: " + itemId);
        }
        return itemMapper.toItemDto(itemStorage.updateItem(itemId, itemDto));
    }

    @Override
    public List<ItemDto> findAllItemsByUserId(Long userId) {
        getUserOrThrow(userId);
        return itemStorage.findAllItemsByUserId(userId).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = getItemOrThrow(itemId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String query = text.toLowerCase(Locale.ROOT);

        return itemStorage.getSearch(query).stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> {
                    String name = item.getName() == null ? "" : item.getName().toLowerCase(Locale.ROOT);
                    String description = item.getDescription() == null ? "" : item.getDescription().toLowerCase(Locale.ROOT);
                    return name.contains(query) || description.contains(query);
                })
                .map(itemMapper::toItemDto)
                .toList();
    }


    private User getUserOrThrow(Long userId) {
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemStorage.findItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Объект не найден"));
    }


}
