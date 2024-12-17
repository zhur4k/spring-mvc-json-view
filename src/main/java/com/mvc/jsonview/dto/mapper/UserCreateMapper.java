package com.mvc.jsonview.dto.mapper;

import com.mvc.jsonview.dto.UserCreateDto;
import com.mvc.jsonview.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserCreateMapper implements Function<UserCreateDto,User> {
    @Override
    public User apply(UserCreateDto userCreateDto) {
        User user = new User();
        user.setAddress(userCreateDto.address());
        user.setPhone(userCreateDto.phone());
        user.setEmail(userCreateDto.email());
        user.setName(userCreateDto.name());
        user.setOrders(userCreateDto.orders());
        return user;
    }
}
