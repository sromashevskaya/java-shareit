package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDto addBooking(Long userId, BookingDto bookingDto) {
        User user = getUserOrThrow(userId);
        Item item = getItemOrThrow(bookingDto.getItemId());

        validateBooking(bookingDto, item, user);

        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);

        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);
        Item item = booking.getItem();

        if (!userRepository.existsById(userId)) {
            throw new BadRequestException("Указанный пользователь не найден");
        }

        if (!userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Указанная вещь не принадлежит пользователю");
        }

        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Статус некорректен");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto findBookingByUserId(Long bookingId, Long userId) {
        return BookingMapper.toBookingResponseDto(getBookingOrThrow(bookingId));
    }

    @Override
    public List<BookingResponseDto> findAllBookingsByBookerId(Long bookerId, String state) {
        getUserOrThrow(bookerId);
        LocalDateTime now = LocalDateTime.now();

        State bookingState;
        try {
            bookingState = State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Некорректное состояние бронирования: " + state);
        }

        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findByBooker_Id(bookerId);
            case CURRENT ->
                    bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(bookerId, now, now);
            case PAST -> bookingRepository.findByBooker_IdAndEndIsBeforeOrderByEndDesc(bookerId, now);
            case FUTURE -> bookingRepository.findByBooker_IdAndStartIsAfterOrderByStartDesc(bookerId, now);
            case WAITING -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> findAllBookingsByOwnerId(Long ownerId, String state) {
        getUserOrThrow(ownerId);
        LocalDateTime now = LocalDateTime.now();

        State bookingState;
        try {
            bookingState = State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Некорректное состояние бронирования: " + state);
        }

        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId);
            case CURRENT ->
                    bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, now, now);
            case PAST -> bookingRepository.findByItem_Owner_IdAndEndIsBeforeOrderByEndDesc(ownerId, now);
            case FUTURE -> bookingRepository.findByItem_Owner_IdAndStartIsAfterOrderByStartDesc(ownerId, now);
            case WAITING -> bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, Status.REJECTED);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
    }

    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Указанная бронь не найдена"));
    }

    private void validateBooking(BookingDto dto, Item item, User booker) {
        if (item.getOwner().getId().equals(booker.getId())) {
            throw new BadRequestException("Данный объект уже принадлежит пользователю");
        }

        if (!item.getAvailable()) {
            throw new BadRequestException("Объект недоступен для бронирования");
        }

        if (dto.getStart().isAfter(dto.getEnd()) || dto.getStart().isEqual(dto.getEnd())) {
            throw new BadRequestException("Некорректные даты начала и окончания бронирования");
        }
    }
}
