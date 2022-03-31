package com.epam.esm.controller;

import com.epam.esm.MJCApplication;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.InvalidPaginationDataException;
import com.epam.esm.service.exception.MissingPageNumberException;
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
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MJCApplication.class)
@ActiveProfiles("template-test")
@Transactional
class UserControllerJdbcTemplateTest {
    @Autowired
    private UserController userController;

    @Test
    void readUserWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> userController.readUserById(974));
    }

    @Test
    void readAllUsersWithInvalidPageParameterTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "-17");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Assertions.assertThrows(InvalidPaginationDataException.class, () -> userController.readAllUsers(pageParameters));
    }

    @Test
    void readAllUsersWithInvalidPageSizeParameterTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "-15");

        Assertions.assertThrows(InvalidPaginationDataException.class, () -> userController.readAllUsers(pageParameters));
    }

    @Test
    void readAllUsersWithoutPaginationParametersTest() {
        Assertions.assertThrows(MissingPageNumberException.class, () -> userController.readAllUsers(new HashMap<>()));
    }

    @Test
    void readAllUserOrdersWithInvalidUserIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> userController.readAllUserOrders(238));
    }
}