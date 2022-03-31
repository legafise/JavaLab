package com.epam.esm.dao.impl;

import com.epam.esm.TestApplication;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("hibernate-test")
@Transactional
class HibernateUserDaoTest {
    @Autowired
    private HibernateUserDao userDao;
    private User testUser;
    private List<User> testUserList;

    @BeforeEach
    void setUp() {
        Tag testTag = new Tag(101, "Tattoo");
        Order testOrder = new Order(101,  new Certificate(101, "TattooLand", "The certificate allows to you make a tattoo",
                new BigDecimal("125.00"), (short) 92, LocalDateTime.parse("2022-01-20T21:00"),
                LocalDateTime.parse("2022-04-20T21:00"), new HashSet<>(Collections.singletonList(testTag))),
                new BigDecimal("125.00"), LocalDateTime.parse("2022-03-20T17:14:42"));
        testUser = new User(101, "Oleg", new BigDecimal("100.00"), Collections.singletonList(testOrder));

        Tag secondTestTag = new Tag(102,"Jumps");
        Tag thirdTestTag = new Tag(103,"Entertainment");
        Order secondTestOrder = new Order(102, new Certificate(102, "Jump park", "Free jumps at trampolines",
                new BigDecimal("35.00"), (short) 30, LocalDateTime.parse("2022-03-15T21:30"),
                LocalDateTime.parse("2022-06-15T21:30"), new HashSet<>(Arrays.asList(secondTestTag, thirdTestTag))),
                new BigDecimal("30.00"), LocalDateTime.parse("2022-04-20T15:12:34"));
        User secondTestUser = new User(102, "Ivan", new BigDecimal("75.78"), Collections.singletonList(secondTestOrder));

        testUserList = Arrays.asList(testUser, secondTestUser);
    }

    @Test
    void findByIdPositiveTest() {
        Assertions.assertEquals(testUser, userDao.findById(101).get());
    }

    @Test
    void findAllTest() {
        Assertions.assertEquals(testUserList, userDao.findAll(1, 2));
    }

    @Test
    void updateUserTest() {
        testUser.setBalance(new BigDecimal("50"));
        Assertions.assertEquals(testUser, userDao.update(testUser));
    }

    @Test
    void addOrderToUserTest() {
        Assertions.assertTrue(userDao.addOrderToUser(101, 103));
    }
}