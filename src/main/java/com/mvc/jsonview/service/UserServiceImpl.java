package com.mvc.jsonview.service;

import com.mvc.jsonview.dto.UserCreateDto;
import com.mvc.jsonview.dto.UserUpdateDto;
import com.mvc.jsonview.dto.mapper.UserCreateMapper;
import com.mvc.jsonview.dto.mapper.UserUpdateMapper;
import com.mvc.jsonview.model.Order;
import com.mvc.jsonview.model.User;
import com.mvc.jsonview.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserCreateMapper userCreateMapper;

    private final UserUpdateMapper userUpdateMapper;

    public UserServiceImpl(UserRepository userRepository, UserCreateMapper userCreateMapper, UserUpdateMapper userUpdateMapper) {
        this.userRepository = userRepository;
        this.userCreateMapper = userCreateMapper;
        this.userUpdateMapper = userUpdateMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateUser(UserUpdateDto user) {
        userRepository.save(userUpdateMapper.apply(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void createUser(UserCreateDto userDto) {
        User user = userCreateMapper.apply(userDto);
        for (Order order : user.getOrders()) {
            order.setUser(user);
        }
        userRepository.save(user);
    }
}
