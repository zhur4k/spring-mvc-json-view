package com.mvc.jsonview.dto.mapper;

import com.mvc.jsonview.dto.UserUpdateDto;
import com.mvc.jsonview.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserUpdateMapper implements Function<UserUpdateDto,User> {
    @Override
    public User apply(UserUpdateDto userUpdateDto) {
        User user = new User();
        user.setAddress(userUpdateDto.address());
        user.setPhone(userUpdateDto.phone());
        user.setEmail(userUpdateDto.email());
        user.setName(userUpdateDto.name());
        return user;
    }
}
