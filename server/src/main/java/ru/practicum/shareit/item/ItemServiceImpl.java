package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.dto.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = getUserOrThrow(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item existingItem = getItemOrThrow(itemId);
        if (!userId.equals(existingItem.getOwner().getId())) {
            throw new NotFoundException("Объект с этим id не найден: " + itemId);
        }

        if (!Objects.isNull(itemDto.getName()) && !itemDto.getName().isBlank()) {
            existingItem.setName(itemDto.getName());
        }
        if (!Objects.isNull(itemDto.getDescription()) && !itemDto.getDescription().isBlank()) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (!Objects.isNull(itemDto.getAvailable())) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(existingItem);
    }

    @Override
    public List<ItemDto> findAllItemsByUserId(Long userId) {
        getUserOrThrow(userId);
        return itemRepository.findAllByOwnerIdOrderById(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = getItemOrThrow(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setComments(commentRepository.findAllByItem_Id(itemId).stream()
                .map(CommentMapper::toCommentResponseDto)
                .toList());
        return itemDto;
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String query = text.toLowerCase(Locale.ROOT);

        return itemRepository.search(query).stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> {
                    String name = item.getName() == null ? "" : item.getName().toLowerCase(Locale.ROOT);
                    String description = item.getDescription() == null ? "" : item.getDescription().toLowerCase(Locale.ROOT);
                    return name.contains(query) || description.contains(query);
                })
                .map(ItemMapper::toItemDto)
                .toList();
    }


    private User getUserOrThrow(Long userId) {
        return (User) userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Объект не найден"));
    }

    @Transactional
    public CommentResponseDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = getItemOrThrow(itemId);
        User user = getUserOrThrow(userId);
        List<Booking> booking = bookingRepository.findByBooker_IdAndItem_IdAndEndIsBeforeAndStatus(userId,
                itemId, LocalDateTime.now(), Status.APPROVED);
        if (booking.isEmpty()) {
            throw new BadRequestException("Запрос некорректный");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponseDto> findCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItem_Id(itemId)
                .stream()
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }
}