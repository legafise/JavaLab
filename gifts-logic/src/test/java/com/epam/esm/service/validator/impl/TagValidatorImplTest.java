package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagValidatorImplTest {
    private TagValidatorImpl tagValidator;
    private Tag testTag;

    @BeforeEach
    void setUp() {
        tagValidator = new TagValidatorImpl();
        testTag = new Tag(1, "Jumps");
    }

    @Test
    void validateTagPositiveTest() {
        Assertions.assertTrue(tagValidator.validateTag(testTag));
    }

    @Test
    void validateTagWithInvalidNameTest() {
        testTag.setName("d");
        Assertions.assertFalse(tagValidator.validateTag(testTag));
    }

    @Test
    void validateTagWithNullNameTest() {
        testTag.setName(null);
        Assertions.assertFalse(tagValidator.validateTag(testTag));
    }
}