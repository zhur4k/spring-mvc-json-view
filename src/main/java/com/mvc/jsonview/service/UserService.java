package com.mvc.jsonview.service;

import com.mvc.jsonview.dto.UserCreateDto;
import com.mvc.jsonview.dto.UserUpdateDto;
import com.mvc.jsonview.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void updateUser(UserUpdateDto user);

    void deleteUser(Long id);

    void createUser(UserCreateDto user);
}
