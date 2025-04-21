package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

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
    public BookingDto addBooking(Long userId, BookingDto bookingDto) {
        User user = getUserOrThrow(userId);
        Item item = getItemOrThrow(bookingDto.getItemId());

        validateBooking(bookingDto, item, user);

        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        booking.setStatus(Status.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);
        Item item = booking.getItem();

        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Указанный пользователь не найден");
        }

        if (!userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Указанная вещь не принадлежит пользователю");
        }

        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Статус некорректен");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto findBookingByUserId(Long bookingId, Long userId) {
        return BookingMapper.toBookingDto(getBookingOrThrow(bookingId));
    }

  /*  @Override
    public List<BookingDto> findAllBookingsByBookerId(Long bookerId, String state) {
        getUserOrThrow(bookerId);
        return getBookingOrThrow(() -> getBookingsByBooker(bookerId, state));
    }

    @Override
    public List<BookingDto> findAllBookingsByBookerId(Long bookerId, String state) {
        getUserOrThrow(bookerId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (State.valueOf(state)) {
            case ALL -> bookingRepository.findByBookerId(bookerId);
            case CURRENT ->
                    bookingRepository.findByBookerIdAndInTimePeriod(bookerId, now, now);
            case PAST -> bookingRepository.findByBookerIdBeforeEnd(bookerId, now);
            case FUTURE -> bookingRepository.findByBookerIdAfterStart(bookerId, now);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.REJECTED);
        };

        return bookings.stream().map(BookingMapper::toBookingDto)
                .collect(toList());
    } */

    @Override
    public List<BookingDto> findAllBookingsByBookerId(Long bookerId, String state) {
        getUserOrThrow(bookerId);
        LocalDateTime now = LocalDateTime.now();

        State bookingState;
        try {
            bookingState = State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Некорректное состояние бронирования: " + state);
        }

        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findByBookerId(bookerId);
            case CURRENT -> bookingRepository.findByBookerIdAndInTimePeriod(bookerId, now, now);
            case PAST -> bookingRepository.findByBookerIdBeforeEnd(bookerId, now);
            case FUTURE -> bookingRepository.findByBookerIdAfterStart(bookerId, now);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.REJECTED);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

  /*  @Override
    public List<BookingDto> findAllBookingsByOwnerId(Long ownerId, String state) {
        getUserOrThrow(ownerId);
        return getBookingOrThrow(() -> getBookingsByOwner(ownerId, state));
    }

    @Override
    public List<BookingDto> findAllBookingsByOwnerId(Long ownerId, String state) {
        getUserOrThrow(ownerId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (State.valueOf(state)) {
            case ALL -> bookingRepository.findByOwnerId(ownerId);
            case CURRENT ->
                    bookingRepository.findByOwnerIdAndInTimePeriod(ownerId, now, now);
            case PAST -> bookingRepository.findByOwnerIdBeforeEnd(ownerId, now);
            case FUTURE -> bookingRepository.findByOwnerIdAfterStart(ownerId, now);
            case WAITING -> bookingRepository.findByOwnerIdAndStatus(ownerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByOwnerIdAndStatus(ownerId, Status.REJECTED);
        };

        return bookings.stream().map(BookingMapper::toBookingDto)
                .collect(toList());
    } */

    @Override
    public List<BookingDto> findAllBookingsByOwnerId(Long ownerId, String state) {
        getUserOrThrow(ownerId);
        LocalDateTime now = LocalDateTime.now();

        State bookingState;
        try {
            bookingState = State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Некорректное состояние бронирования: " + state);
        }

        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findByOwnerId(ownerId);
            case CURRENT -> bookingRepository.findByOwnerIdAndInTimePeriod(ownerId, now, now);
            case PAST -> bookingRepository.findByOwnerIdBeforeEnd(ownerId, now);
            case FUTURE -> bookingRepository.findByOwnerIdAfterStart(ownerId, now);
            case WAITING -> bookingRepository.findByOwnerIdAndStatus(ownerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByOwnerIdAndStatus(ownerId, Status.REJECTED);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList(); // можно заменить на .collect(Collectors.toList()) при необходимости
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
            throw new ValidationException("Данный объект уже принадлежит пользователю");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Объект недоступен для бронирования");
        }

        if (dto.getStart().isAfter(dto.getEnd()) || dto.getStart().isEqual(dto.getEnd())) {
            throw new ValidationException("Некорректные даты начала и окончания бронирования");
        }
    }

  /*  private List<Booking> getBookingsByBooker(Long bookerId, String state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (State.valueOf(state)) {
            case ALL -> bookingRepository.findByBookerId(bookerId);
            case CURRENT -> bookingRepository.findByBookerIdAndInTimePeriod(bookerId, now, now);
            case PAST -> bookingRepository.findByBookerIdBeforeEnd(bookerId, now);
            case FUTURE -> bookingRepository.findByBookerIdAfterStart(bookerId, now);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.REJECTED);
        };
    }

    private List<Booking> getBookingsByOwner(Long ownerId, String state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (State.valueOf(state)) {
            case ALL -> bookingRepository.findByOwnerId(ownerId);
            case CURRENT -> bookingRepository.findByOwnerIdAndInTimePeriod(ownerId, now, now);
            case PAST -> bookingRepository.findByOwnerIdBeforeEnd(ownerId, now);
            case FUTURE -> bookingRepository.findByOwnerIdAfterStart(ownerId, now);
            case WAITING -> bookingRepository.findByOwnerIdAndStatus(ownerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByOwnerIdAndStatus(ownerId, Status.REJECTED);
        };
    } */
}
