package com.mvc.jsonview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvc.jsonview.dto.UserCreateDto;
import com.mvc.jsonview.dto.UserUpdateDto;
import com.mvc.jsonview.model.Order;
import com.mvc.jsonview.model.User;
import com.mvc.jsonview.model.Views;
import com.mvc.jsonview.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsersSuccess() throws Exception {
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

        MappingJacksonValue result = new MappingJacksonValue(users);
        result.setSerializationView(Views.UserDetails.class);

        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writerWithView(Views.UserSummary.class).writeValueAsString(users)));
    }

    @Test
    void testGetAllUsersException() throws Exception {
        when(userService.getAllUsers()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetUserSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Иван Иванов");
        user.setAddress("г. Москва, ул. Ленина, 1");
        user.setPhone("+7-900-123-45-67");
        user.setEmail("ivan.ivanov@example.com");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writerWithView(Views.UserDetails.class).writeValueAsString(user)));
    }

    @Test
    void testGetUserException() throws Exception {
        when(userService.getUserById(1L)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto(
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
        "+7-900-123-45-67",
        "ivan.ivanov@example.com",
                null
        );
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated());
        verify(userService,times(1)).createUser(any());
    }

    @Test
    void testCreateUserException() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto(
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com",
                null
        );

        doThrow(RuntimeException.class).when(userService).createUser(userCreateDto);

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void teatUpdateUserSuccess() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto(
                1L,
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com"
        );

        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk());
        verify(userService,times(1)).updateUser(any());
    }

    @Test
    void teatUpdateUserException() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto(
                1L,
                "Иван Иванов",
                "г. Москва, ул. Ленина, 1",
                "+7-900-123-45-67",
                "ivan.ivanov@example.com"
        );

        doThrow(RuntimeException.class).when(userService).updateUser(userUpdateDto);

        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/users/delete/" + userId))
                .andExpect(status().isOk());
        verify(userService,times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUserException() throws Exception {
        Long userId = 1L;
        doThrow(RuntimeException.class).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/delete/" + userId))
                .andExpect(status().isInternalServerError());
        verify(userService,times(1)).deleteUser(userId);
    }
}