package com.mvc.jsonview.service;

import com.mvc.jsonview.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    void createUser(User user);
}
