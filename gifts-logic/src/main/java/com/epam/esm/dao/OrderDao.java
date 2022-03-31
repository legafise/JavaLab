package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> findById(long id);

    void add(Order order);

    long findMaxOrderId();
}
