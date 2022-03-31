package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.checker.TagDuplicationChecker;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.EntityDuplicationException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.UnknownEntityException;
import com.epam.esm.service.handler.PaginationParametersHandler;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagServiceImplTest {
    private TagServiceImpl tagService;
    private TagValidator tagValidator;
    private TagDuplicationChecker tagDuplicationChecker;
    private PaginationParametersHandler paginationParametersHandler;
    private TagDao tagDao;
    private Tag firstTestTag;
    private List<Tag> tags;

    @BeforeEach
    void setUp() {
        tagValidator = mock(TagValidator.class);
        tagDuplicationChecker = mock(TagDuplicationChecker.class);
        tagDao = mock(TagDao.class);
        paginationParametersHandler = mock(PaginationParametersHandler.class);
        tagService = new TagServiceImpl(tagValidator, tagDuplicationChecker, tagDao, paginationParametersHandler);

        firstTestTag = new Tag(1, "Jumps");
        Tag secondTestTag = new Tag(2, "Fly");
        tags = Arrays.asList(firstTestTag, secondTestTag);
    }

    @Test
    void addTagPositiveTest() {
        when(tagValidator.validateTag(firstTestTag)).thenReturn(true);
        when(tagDuplicationChecker.checkTagForDuplication(firstTestTag)).thenReturn(true);
        when(tagDao.findByName(firstTestTag.getName())).thenReturn(Optional.of(firstTestTag));
        Assertions.assertEquals(tagService.addTag(firstTestTag), firstTestTag);
    }

    @Test
    void addTagWithInvalidTagTest() {
        when(tagValidator.validateTag(firstTestTag)).thenReturn(false);
        when(tagDuplicationChecker.checkTagForDuplication(firstTestTag)).thenReturn(true);
        when(tagDao.findByName(firstTestTag.getName())).thenReturn(Optional.of(firstTestTag));
        Assertions.assertThrows(InvalidEntityException.class, () -> tagService.addTag(firstTestTag));
    }

    @Test
    void addDuplicateTagTest() {
        when(tagValidator.validateTag(firstTestTag)).thenReturn(true);
        when(tagDuplicationChecker.checkTagForDuplication(firstTestTag)).thenReturn(false);
        when(tagDao.findByName(firstTestTag.getName())).thenReturn(Optional.of(firstTestTag));
        Assertions.assertThrows(EntityDuplicationException.class, () -> tagService.addTag(firstTestTag));
    }

    @Test
    void findAllTagsTest() {
        Map<String, String> paginationParameters = new HashMap<>();
        paginationParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        paginationParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Map<String, Integer> handledPaginationParameters = new HashMap<>();
        handledPaginationParameters.put(PaginationConstant.PAGE_PARAMETER, 1);
        handledPaginationParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, 2);

        when(tagDao.findAll(1,2)).thenReturn(tags);
        when(paginationParametersHandler.handlePaginationParameters(paginationParameters)).thenReturn(handledPaginationParameters);
        Assertions.assertEquals(tagService.findAllTags(paginationParameters), tags);
    }

    @Test
    void findTagByIdTest() {
        when(tagDao.findById(1)).thenReturn(Optional.of(firstTestTag));
        Assertions.assertEquals(tagService.findTagById(1), firstTestTag);
    }

    @Test
    void findTagWithInvalidIdTest() {
        when(tagDao.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownEntityException.class, () -> tagService.findTagById(1));
    }

    @Test
    void findTagByNameTest() {
        when(tagDao.findByName("Jumps")).thenReturn(Optional.of(firstTestTag));
        Assertions.assertEquals(tagService.findTagByName("Jumps"), firstTestTag);
    }

    @Test
    void findTagWithInvalidNameTest() {
        when(tagDao.findByName("Jumps")).thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownEntityException.class, () -> tagService.findTagByName("Jumps"));
    }

    @Test
    void removeTagWithInvalidIdTest() {
        when(tagDao.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownEntityException.class ,() -> tagService.removeTagById(1));
    }

    @Test
    void updateTagPositiveTest() {
        firstTestTag.setName("Test");
        when(tagValidator.validateTag(firstTestTag)).thenReturn(true);
        when(tagDuplicationChecker.checkTagForDuplication(firstTestTag)).thenReturn(true);
        when(tagDao.findById(1)).thenReturn(Optional.of(firstTestTag));
        Assertions.assertEquals(tagService.updateTag(firstTestTag), firstTestTag);
    }

    @Test
    void updateTagWithInvalidTagTest() {
        firstTestTag.setName("");
        when(tagValidator.validateTag(firstTestTag)).thenReturn(false);
        when(tagDuplicationChecker.checkTagForDuplication(firstTestTag)).thenReturn(true);
        when(tagDao.findById(1)).thenReturn(Optional.of(firstTestTag));
        Assertions.assertThrows(InvalidEntityException.class, () -> tagService.updateTag(firstTestTag));
    }

    @Test
    void updateTagWithDuplicationTest() {
        firstTestTag.setName("Fly");
        when(tagValidator.validateTag(firstTestTag)).thenReturn(true);
        when(tagDuplicationChecker.checkTagForDuplication(firstTestTag)).thenReturn(false);
        when(tagDao.findById(1)).thenReturn(Optional.of(firstTestTag));
        Assertions.assertThrows(EntityDuplicationException.class, () -> tagService.updateTag(firstTestTag));
    }
}