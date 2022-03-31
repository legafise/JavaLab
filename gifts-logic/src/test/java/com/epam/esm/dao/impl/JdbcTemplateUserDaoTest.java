package com.epam.esm.dao.impl;

import com.epam.esm.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("template-test")
@Transactional
class JdbcTemplateUserDaoTest {
    @Autowired
    private JdbcTemplateUserDao userDao;

    @Test
    void addOrderToUserTest() {
        Assertions.assertTrue(userDao.addOrderToUser(101, 103));
    }
}