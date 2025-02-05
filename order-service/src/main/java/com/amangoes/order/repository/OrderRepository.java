package com.amangoes.order.repository;

import com.amangoes.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderNumber(String orderNumber);
}

