package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {
    Tag addTag(Tag tag);

    List<Tag> findAllTags(Map<String, String> paginationParameters);

    Tag findTagById(long id);

    Tag findTagByName(String name);

    Tag updateTag(Tag tag);

    void removeTagById(long id);

    void addTagIfNotExists(Tag tag);

    Tag findWidelyUsedTag();
}
