package com.epam.esm.controller;

import com.epam.esm.MJCApplication;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.UnknownEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MJCApplication.class)
@ActiveProfiles("hibernate-test")
@Transactional
class OrderControllerHibernateTest {
    @Autowired
    private OrderController orderController;
    private Order testOrder;
    private EntityModel<Order> testOrderEntityModel;

    @BeforeEach
    void setUp() {
        Tag testTag = new Tag(101, "Tattoo");
        testOrder = new Order(101,  new Certificate(101, "TattooLand", "The certificate allows to you make a tattoo",
                new BigDecimal("125.00"), (short) 92, LocalDateTime.parse("2022-01-20T21:00"),
                LocalDateTime.parse("2022-04-20T21:00"), new HashSet<>(Collections.singletonList(testTag))),
                new BigDecimal("125.00"), LocalDateTime.parse("2022-03-20T17:14:42"));

        testOrderEntityModel = EntityModel.of(testOrder);
        testOrderEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .readCertificateById(testOrder.getCertificate().getId())).withRel("Ordered certificate(id = 101) info"));
    }

    @Test
    void readOrderTest() {
        Assertions.assertEquals(testOrderEntityModel, orderController.readOrder(101));
    }

    @Test
    void readOrderWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> orderController.readOrder(749));
    }
}