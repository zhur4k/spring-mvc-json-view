package com.mvc.jsonview.service;

import com.mvc.jsonview.dto.UserCreateDto;
import com.mvc.jsonview.dto.UserUpdateDto;
import com.mvc.jsonview.dto.mapper.UserCreateMapper;
import com.mvc.jsonview.dto.mapper.UserUpdateMapper;
import com.mvc.jsonview.model.Order;
import com.mvc.jsonview.model.User;
import com.mvc.jsonview.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserUpdateMapper userUpdateMapper;

    @Mock
    private UserCreateMapper userCreateMapper;

    @Test
    void getAllUsersSuccess() {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Иван Иванов");
        user1.setAddress("г. Москва, ул. Ленина, 1");
        user1.setPhone("+7-900-123-45-67");
        user1.setEmail("ivan.ivanov@example.com");

        Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderNumber(1001L);
        order1.setTotalPrice(BigDecimal.valueOf(1500.75));
        order1.setStatus("PAID");
        order1.setUser(user1);

        user1.setOrders(List.of(order1));

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Мария Смирнова");
        user2.setAddress("г. Санкт-Петербург, пр. Невский, 25");
        user2.setPhone("+7-911-987-65-43");
        user2.setEmail("maria.smirnova@example.com");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderNumber(1002L);
        order2.setTotalPrice(BigDecimal.valueOf(2500.50));
        order2.setStatus("PENDING");
        order2.setUser(user2);

        Order order3 = new Order();
        order3.setId(3L);
        order3.setOrderNumber(1003L);
        order3.setTotalPrice(BigDecimal.valueOf(3000.00));
        order3.setStatus("CANCELLED");
        order3.setUser(user2);

        user2.setOrders(List.of(order2, order3));

        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        List<User> resultUsers = userService.getAllUsers();
        assertEquals(user1, resultUsers.get(0));
        assertEquals(user2, resultUsers.get(1));
    }

    @Test
    void getAllUsersException() {
        when(userRepository.findAll()).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.getAllUsers());
    }
    @Test
    void getUserByIdSuccess() {

        User user = new User();
        user.setId(1L);
        user.setName("Иван Иванов");
        user.setAddress("г. Москва, ул. Ленина, 1");
        user.setPhone("+7-900-123-45-67");
        user.setEmail("ivan.ivanov@example.com");

        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber(1001L);
        order.setTotalPrice(BigDecimal.valueOf(1500.75));
        order.setStatus("PAID");
        order.setUser(user);

        user.setOrders(List.of(order));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));;
        User resultUser = userService.getUserById(1L);

        assertEquals(user, resultUser);
        assertEquals(order, resultUser.getOrders().get(0));
    }

    @Test
    void getUserByIdsException() {
        when(userRepository.findById(2L)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.getUserById(2L));
    }

    @Test
    void updateUserSuccess() {
        UserUpdateDto user = new UserUpdateDto(
                1L,
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com"
        );

        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName("Иван Иванов");
        when(userUpdateMapper.apply(user)).thenReturn(expectedUser);

        userService.updateUser(user);

        verify(userRepository, times(1)).save(expectedUser);
    }

    @Test
    void updateUsersException() {
        UserUpdateDto user = new UserUpdateDto(
                1L,
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com"
        );

        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName("Иван Иванов");
        when(userUpdateMapper.apply(user)).thenReturn(expectedUser);

        doThrow(new RuntimeException("Database error")).when(userRepository).save(expectedUser);

        assertThrows(RuntimeException.class, () -> userService.updateUser(user));
    }

    @Test
    void deleteUserSuccess() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUsersException() {
        doThrow(RuntimeException.class).when(userRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void createUserSuccess() {
        UserCreateDto userDto = new UserCreateDto(
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com",
                null
        );

        User expectedUser = new User();
        expectedUser.setName("Иван Иванов");

        when(userCreateMapper.apply(userDto)).thenReturn(expectedUser);

        userService.createUser(userDto);

        verify(userRepository, times(1)).save(expectedUser);
    }

    @Test
    void createUsersException() {
        UserCreateDto userDto = new UserCreateDto(
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com",
                null
        );

        User expectedUser = new User();
        expectedUser.setName("Иван Иванов");

        when(userCreateMapper.apply(userDto)).thenReturn(expectedUser);

        doThrow(new RuntimeException("Database error")).when(userRepository).save(expectedUser);

        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
    }
}