package com.mvc.jsonview.dto;

import com.mvc.jsonview.model.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserCreateDto (
    @NotNull
    @Size(min = 3, max = 50,message = "Имя пользователя должно содержать от 3 до 50 символов")
    String name,

    String address,

    String phone,

    @NotNull(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    String email,

    List<Order> orders
)
{
}
