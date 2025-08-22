package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = getUserOrThrow(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        itemRequest = itemRequestRepository.save(itemRequest);
        List<Item> items = itemRepository.findByRequest(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestResponseDto> findAllItemRequestsByRequestorId(Long userId) {
        User user = getUserOrThrow(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorOrderByCreatedDesc(user);
        List<ItemRequestResponseDto> result = new ArrayList<>();

        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findByRequest(itemRequest);
            ItemRequestResponseDto dto = ItemRequestMapper.toItemRequestDto(itemRequest, items);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<ItemRequestResponseDto> findAllItemRequestsExceptUserId(Long userId) {
        User user = getUserOrThrow(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorNotOrderByCreatedDesc(user);
        List<ItemRequestResponseDto> result = new ArrayList<>();

        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findByRequest(itemRequest);
            ItemRequestResponseDto dto = ItemRequestMapper.toItemRequestDto(itemRequest, items);
            result.add(dto);
        }

        return result;
    }

    @Override
    public ItemRequestResponseDto findItemRequestById(Long requestId, Long userId) {
        getUserOrThrow(userId);
        ItemRequest itemRequest = getItemRequestOrThrow(requestId);
        List<Item> items = itemRepository.findByRequest(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }


    private User getUserOrThrow(Long userId) {
        return (User) userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private ItemRequest getItemRequestOrThrow(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }

}

