package com.mvc.jsonview.repository;

import com.mvc.jsonview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
