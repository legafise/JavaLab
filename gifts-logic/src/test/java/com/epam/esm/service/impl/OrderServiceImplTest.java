package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.NotEnoughMoneyException;
import com.epam.esm.service.exception.UnknownEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    private OrderServiceImpl orderService;
    private OrderDao orderDao;
    private UserDao userDao;
    private UserService userService;
    private CertificateService certificateService;
    private Order testOrder;
    private Certificate testCertificate;
    private User testUser;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        userDao = mock(UserDao.class);
        userService = mock(UserService.class);
        certificateService = mock(CertificateService.class);
        orderService = new OrderServiceImpl(orderDao, userDao, userService, certificateService);

        testUser = new User(1, "testUser", new BigDecimal("100"));

        Tag firstTestTag = new Tag(1, "Jumps");
        Tag secondTestTag = new Tag(3, "Entertainment");

        testCertificate = new Certificate(2, "Jump park", "Free jumps for your health!",
                new BigDecimal("30"), (short) 30, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(firstTestTag, secondTestTag)));
        testOrder = new Order(1, testCertificate, new BigDecimal("30"), LocalDateTime.now());
    }

    @Test
    void findOrderByIdPositiveTest() {
        when(orderDao.findById(1)).thenReturn(Optional.of(testOrder));
        Assertions.assertEquals(testOrder, orderService.findOrderById(1));
    }

    @Test
    void findOrderWithInvalidIdTest() {
        when(orderDao.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownEntityException.class, () -> orderService.findOrderById(1));
    }

    @Test
    void createOrderPositiveTest() {
        when(certificateService.findCertificateById(1)).thenReturn(testCertificate);
        when(userService.findUserById(1)).thenReturn(testUser);
        doNothing().when(orderDao).add(isA(Order.class));
        when(orderDao.findMaxOrderId()).thenReturn(1L);
        when(userDao.update(isA(User.class))).thenReturn(testUser);
        when(userDao.addOrderToUser(testUser.getId(), 1)).thenReturn(true);
        when(orderDao.findById(1)).thenReturn(Optional.of(testOrder));

        Assertions.assertEquals(testOrder, orderService.createOrder(1, 1));
    }

    @Test
    void createOrderWithInvalidUserBalanceTest() {
        testUser.setBalance(new BigDecimal("20"));
        when(certificateService.findCertificateById(1)).thenReturn(testCertificate);
        when(userService.findUserById(1)).thenReturn(testUser);

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> orderService.createOrder(1, 1));
    }
}