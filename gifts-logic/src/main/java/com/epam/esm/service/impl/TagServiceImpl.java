package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.checker.TagDuplicationChecker;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.EntityDuplicationException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.UnknownEntityException;
import com.epam.esm.service.handler.PaginationParametersHandler;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private static final String NONEXISTENT_TAG_MESSAGE = "nonexistent.tag";
    private static final String REPEATING_TAG_MESSAGE = "repeating.tag";
    private static final String INVALID_TAG_MESSAGE = "invalid.tag";
    private final TagValidator tagValidator;
    private final TagDuplicationChecker tagDuplicationChecker;
    private final TagDao tagDao;
    private final PaginationParametersHandler paginationParametersHandler;

    @Autowired
    public TagServiceImpl(TagValidator tagValidator, TagDuplicationChecker tagDuplicationChecker,
                          TagDao tagDao, PaginationParametersHandler paginationParametersHandler) {
        this.tagValidator = tagValidator;
        this.tagDuplicationChecker = tagDuplicationChecker;
        this.tagDao = tagDao;
        this.paginationParametersHandler = paginationParametersHandler;
    }

    @Override
    @Transactional
    public Tag addTag(Tag tag) {
        if (!tagValidator.validateTag(tag)) {
            throw new InvalidEntityException(Tag.class, INVALID_TAG_MESSAGE);
        }

        if (!tagDuplicationChecker.checkTagForDuplication(tag)) {
            throw new EntityDuplicationException(Tag.class, REPEATING_TAG_MESSAGE);
        }

        tagDao.add(tag);
        return findTagByName(tag.getName());
    }

    @Override
    public List<Tag> findAllTags(Map<String, String> paginationParameters) {
        Map<String, Integer> handledPaginationParameters = paginationParametersHandler.handlePaginationParameters(paginationParameters);

        return tagDao.findAll(handledPaginationParameters.get(PaginationConstant.PAGE_PARAMETER),
                handledPaginationParameters.get(PaginationConstant.PAGE_SIZE_PARAMETER));
    }

    @Override
    public Tag findTagById(long id) {
        Optional<Tag> tag = tagDao.findById(id);
        if (!tag.isPresent()) {
            throw new UnknownEntityException(Tag.class, NONEXISTENT_TAG_MESSAGE);
        }

        return tag.get();
    }

    @Override
    public Tag findTagByName(String name) {
        Optional<Tag> tag = tagDao.findByName(name);
        if (!tag.isPresent()) {
            throw new UnknownEntityException(Tag.class, NONEXISTENT_TAG_MESSAGE);
        }

        return tag.get();
    }

    @Override
    public Tag updateTag(Tag tag) {
        if (!tagValidator.validateTag(tag)) {
            throw new InvalidEntityException(Tag.class, INVALID_TAG_MESSAGE);
        }

        if (!tagDuplicationChecker.checkTagForDuplication(tag)) {
            throw new EntityDuplicationException(Tag.class, REPEATING_TAG_MESSAGE);
        }

        tagDao.update(tag);
        return findTagById(tag.getId());
    }

    @Override
    @Transactional
    public void removeTagById(long id) {
        if (!tagDao.findById(id).isPresent()) {
            throw new UnknownEntityException(Tag.class, NONEXISTENT_TAG_MESSAGE);
        }

        tagDao.removeTagFromCertificates(id);
        tagDao.remove(id);
    }

    @Override
    @Transactional
    public void addTagIfNotExists(Tag tag) {
        Optional<Tag> searchedTag = tagDao.findByName(tag.getName());
        if (!searchedTag.isPresent()) {
            addTag(tag);
        }
    }

    @Override
    public Tag findWidelyUsedTag() {
        return tagDao.findWidelyUsedTag();
    }
}
