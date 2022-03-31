package com.epam.esm.service.checker.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagDuplicationCheckerImplTest {
    private TagDuplicationCheckerImpl tagDuplicationChecker;
    private Tag firstTestTag;
    private Tag secondTestTag;
    private TagDao tagDao;

    @BeforeEach
    void setUp() {
        tagDao = mock(TagDao.class);
        tagDuplicationChecker = new TagDuplicationCheckerImpl(tagDao);
        firstTestTag = new Tag(1, "TestTag1");
        secondTestTag = new Tag(2, "TestTag2");
    }

    @Test
    void checkTagForDuplicationWithNewTest() {
        when(tagDao.findByName(firstTestTag.getName())).thenReturn(Optional.empty());
        Assertions.assertTrue(tagDuplicationChecker.checkTagForDuplication(firstTestTag));
    }

    @Test
    void checkTagForDuplicationWhileUpdateTest() {
        when(tagDao.findByName(firstTestTag.getName())).thenReturn(Optional.of(firstTestTag));
        Assertions.assertTrue(tagDuplicationChecker.checkTagForDuplication(firstTestTag));
    }

    @Test
    void checkTagForDuplicationBadTest() {
        when(tagDao.findByName(firstTestTag.getName())).thenReturn(Optional.of(secondTestTag));
        Assertions.assertFalse(tagDuplicationChecker.checkTagForDuplication(firstTestTag));
    }
}