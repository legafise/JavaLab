package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.UnknownEntityException;
import com.epam.esm.service.handler.PaginationParametersHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserDao userDao;
    private PaginationParametersHandler paginationParametersHandler;
    private User firstTestUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        paginationParametersHandler = mock(PaginationParametersHandler.class);
        userService = new UserServiceImpl(userDao, paginationParametersHandler);
        firstTestUser = new User(1, "testUser", new BigDecimal("100"));
        User secondTestUser = new User(2, "secondUSer", new BigDecimal("67.4"));
        userList = Arrays.asList(firstTestUser, secondTestUser);
    }

    @Test
    void findUserByIdPositiveTest() {
        when(userDao.findById(1)).thenReturn(Optional.of(firstTestUser));
        Assertions.assertEquals(firstTestUser, userService.findUserById(1));
    }

    @Test
    void findUserWithInvalidIdTest() {
        when(userDao.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownEntityException.class, () -> userService.findUserById(1));
    }

    @Test
    void findAllUsersPositiveTest() {
        Map<String, String> paginationParameters = new HashMap<>();
        paginationParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        paginationParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Map<String, Integer> handledPaginationParameters = new HashMap<>();
        handledPaginationParameters.put(PaginationConstant.PAGE_PARAMETER, 1);
        handledPaginationParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, 2);

        when(userDao.findAll(1,2)).thenReturn(userList);
        when(paginationParametersHandler.handlePaginationParameters(paginationParameters)).thenReturn(handledPaginationParameters);
        Assertions.assertEquals(userList, userService.findAllUsers(paginationParameters));
    }
}