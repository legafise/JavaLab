package com.epam.esm.controller;

import com.epam.esm.MJCApplication;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.EntityDuplicationException;
import com.epam.esm.service.exception.InvalidPaginationDataException;
import com.epam.esm.service.exception.MissingPageNumberException;
import com.epam.esm.service.exception.UnknownEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MJCApplication.class)
@ActiveProfiles("template-test")
@Transactional
class TagControllerJdbcTemplateTest {
    @Autowired
    private TagController tagController;
    private Tag testTag;
    private List<Tag> testTagList;

    @BeforeEach
    void setUp() {
        testTag = new Tag(101, "Tattoo");
        Tag secondTestTag = new Tag(102, "Jumps");
        testTagList = Arrays.asList(testTag, secondTestTag);
    }

    @Test
    void readAllTagsTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Assertions.assertEquals(testTagList, tagController.readAllTags(pageParameters));
    }

    @Test
    void readAllTagsWithInvalidPageTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "-1");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Assertions.assertThrows(InvalidPaginationDataException.class, () -> tagController.readAllTags(pageParameters));
    }

    @Test
    void readAllTagsWithInvalidPageSizeTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "-2");

        Assertions.assertThrows(InvalidPaginationDataException.class, () -> tagController.readAllTags(pageParameters));
    }

    @Test
    void readAllTagsWithoutPaginationParametersTest() {
        Assertions.assertThrows(MissingPageNumberException.class, () -> tagController.readAllTags(Collections.emptyMap()));
    }

    @Test
    void readWidelyUsedTagTest() {
        Assertions.assertEquals(testTag, tagController.readWidelyUsedTag());
    }

    @Test
    void readTagTest() {
        Assertions.assertEquals(testTag, tagController.readTag(101));
    }

    @Test
    void readTagWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> tagController.readTag(600));
    }

    @Test
    void updateTagPositiveTest() {
        testTag.setName("SuperTattoo");
        Assertions.assertEquals(testTag, tagController.updateTag(new Tag("SuperTattoo"), 101));
    }

    @Test
    void updateTagToExistedTagTest() {
        Assertions.assertThrows(EntityDuplicationException.class, () -> tagController.updateTag(new Tag("Jumps"), 101));
    }

    @Test
    void deleteTagTest() {
        tagController.deleteTag(104);
    }

    @Test
    void deleteTagWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> tagController.deleteTag(600));
    }
}