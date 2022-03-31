package com.epam.esm.service;

import com.epam.esm.entity.Order;

public interface OrderService {
    Order findOrderById(long orderId);

    Order createOrder(long userId, long certificateId);
}
