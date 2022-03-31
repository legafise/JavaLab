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
@ActiveProfiles("template-test")
@Transactional
class OrderControllerJdbcTemplateTest {
    @Autowired
    private OrderController orderController;

    @Test
    void readOrderWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> orderController.readOrder(749));
    }
}