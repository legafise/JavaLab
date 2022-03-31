package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.NotEnoughMoneyException;
import com.epam.esm.service.exception.UnknownEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String NONEXISTENT_ORDER_MESSAGE = "nonexistent.order";
    private static final String NOT_ENOUGH_MONEY_MESSAGE = "not.enough.money";
    private static final BigDecimal MIN_ALLOWABLE_BALANCE = new BigDecimal("0");
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final UserService userService;
    private final CertificateService certificateService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, UserService userService, CertificateService certificateService) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.userService = userService;
        this.certificateService = certificateService;
    }

    @Override
    public Order findOrderById(long orderId) {
        Optional<Order> order = orderDao.findById(orderId);
        if (!order.isPresent()) {
            throw new UnknownEntityException(Order.class, NONEXISTENT_ORDER_MESSAGE);
        }

        return order.get();
    }

    @Override
    @Transactional
    public Order createOrder(long userId, long certificateId) {
        Certificate certificate = certificateService.findCertificateById(certificateId);
        User user = userService.findUserById(userId);
        Order order = new Order(certificate, certificate.getPrice(), LocalDateTime.now());
        BigDecimal residualBalance = user.getBalance().subtract(order.getPrice());

        if (residualBalance.compareTo(MIN_ALLOWABLE_BALANCE) < 0) {
            throw new NotEnoughMoneyException(NOT_ENOUGH_MONEY_MESSAGE);
        }

        orderDao.add(order);
        long lastAddedOrderId = orderDao.findMaxOrderId();
        user.setBalance(residualBalance);
        userDao.update(user);
        userDao.addOrderToUser(user.getId(), lastAddedOrderId);

        return findOrderById(lastAddedOrderId);
    }
}
