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
@ActiveProfiles("hibernate-test")
@Transactional
class UserControllerHibernateTest {
    @Autowired
    private UserController userController;
    private EntityModel<User> testUserModel;
    private User testUser;
    private List<EntityModel<Order>> testUserOrdersModelList;
    private List<EntityModel<User>> testUserModelList;

    @BeforeEach
    void setUp() {
        Tag testTag = new Tag(101, "Tattoo");
        Tag secondTestTag = new Tag(102,"Jumps");
        Tag thirdTestTag = new Tag(103,"Entertainment");

        Order testOrder = new Order(101,  new Certificate(101, "TattooLand", "The certificate allows to you make a tattoo",
                new BigDecimal("125.00"), (short) 92, LocalDateTime.parse("2022-01-20T21:00"),
                LocalDateTime.parse("2022-04-20T21:00"), new HashSet<>(Collections.singletonList(testTag))),
                new BigDecimal("125.00"), LocalDateTime.parse("2022-03-20T17:14:42"));
        testUser = new User(101, "Oleg", new BigDecimal("100.00"), Collections.singletonList(testOrder));
        testUserModel = EntityModel.of(testUser);
        testUserModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                .readOrder(101)).withRel("Order(id = 101) info"));

        Order secondTestOrder = new Order(102, new Certificate(102, "Jump park", "Free jumps at trampolines",
                new BigDecimal("35.00"), (short) 30, LocalDateTime.parse("2022-03-15T21:30"),
                LocalDateTime.parse("2022-06-15T21:30"), new HashSet<>(Arrays.asList(secondTestTag, thirdTestTag))),
                new BigDecimal("30.00"), LocalDateTime.parse("2022-04-20T15:12:34"));
        User secondTestUser = new User(102, "Ivan", new BigDecimal("75.78"), Collections.singletonList(secondTestOrder));
        EntityModel<User> secondTestUserModel = EntityModel.of(secondTestUser);
        secondTestUserModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                .readOrder(102)).withRel("Order(id = 102) info"));

        EntityModel<Order> testOrderEntityModel = EntityModel.of(testOrder);
        testOrderEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .readCertificateById(testOrder.getCertificate().getId())).withRel("Ordered certificate(id = 101) info"));

        testUserModelList = Arrays.asList(testUserModel, secondTestUserModel);
        testUserOrdersModelList = Collections.singletonList(testOrderEntityModel);
    }

    @Test
    void readUserByIdTest() {
        Assertions.assertEquals(testUserModel, userController.readUserById(101));
    }

    @Test
    void readUserWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> userController.readUserById(974));
    }

    @Test
    void readAllUsersTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Assertions.assertEquals(testUserModelList, userController.readAllUsers(pageParameters));
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
    void readAllUserOrdersTest() {
        Assertions.assertEquals(testUserOrdersModelList, userController.readAllUserOrders(101));
    }

    @Test
    void readAllUserOrdersWithInvalidUserIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> userController.readAllUserOrders(238));
    }
}