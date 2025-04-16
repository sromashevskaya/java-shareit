package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemDto addItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDto> findllItemsByUserId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable("itemId") Long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(@RequestParam String text) {
        return itemService.getSearch(text);
    }
}
