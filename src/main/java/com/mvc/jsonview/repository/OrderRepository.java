package com.mvc.jsonview.repository;

import com.mvc.jsonview.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
