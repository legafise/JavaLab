package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagMapperImpl;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile({"template", "template-test"})
public class JdbcTemplateTagDao implements TagDao {
    private static final String ADD_TAG_SQL = "INSERT INTO tags (name) VALUES (?)";
    private static final String FIND_TAG_BY_ID_SQL = "SELECT tags.id AS tag_id, tags.name AS tag_name FROM tags WHERE tags.id = ?";
    private static final String FIND_TAG_BY_NAME_SQL = "SELECT tags.id AS tag_id, tags.name AS tag_name FROM tags WHERE tags.name = ?";
    private static final String FIND_ALL_TAGS_SQL = "SELECT tags.id AS tag_id, tags.name AS tag_name FROM tags ORDER BY tags.id ASC LIMIT ? OFFSET ?";
    private static final String REMOVE_TAG_BY_ID_SQL = "DELETE FROM tags WHERE id = ?";
    private static final String REMOVE_TAG_FROM_CERTIFICATES_BY_ID_SQL = "DELETE FROM gift_tags WHERE gift_tags.tag_id = ?";
    private static final String UPDATE_TAG_SQL = "UPDATE tags SET name = ? WHERE id = ?";
    private static final String FIND_WIDELY_USED_TAG = "SELECT tag_id FROM (SELECT tags.id AS tag_id, SUM(orders.price)" +
            " AS tag_orders_price FROM orders INNER JOIN gift_certificates ON orders.certificate_id = gift_certificates.id" +
            " LEFT JOIN gift_tags ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON tags.id = gift_tags.tag_id" +
            " GROUP BY tag_id ORDER BY tag_orders_price DESC LIMIT 1) A";
    private final TagMapperImpl tagMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateTagDao(TagMapperImpl tagMapper, JdbcTemplate jdbcTemplate) {
        this.tagMapper = tagMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Tag tag) {
        jdbcTemplate.update(ADD_TAG_SQL, tag.getName());
    }

    @Override
    public Optional<Tag> findById(long id) {
        List<Tag> tagList = jdbcTemplate.query(FIND_TAG_BY_ID_SQL, tagMapper, id);

        return tagList.isEmpty() ? Optional.empty()
                : Optional.of(tagList.get(0));
    }

    @Override
    public Optional<Tag> findByName(String name) {
        List<Tag> tagList = jdbcTemplate.query(FIND_TAG_BY_NAME_SQL, tagMapper, name);

        return tagList.isEmpty() ? Optional.empty()
                : Optional.of(tagList.get(0));
    }

    @Override
    public List<Tag> findAll(int page, int pageSize) {
        return jdbcTemplate.query(FIND_ALL_TAGS_SQL, new Object[]{pageSize, (page - 1) * pageSize}, tagMapper);
    }

    @Override
    public Tag update(Tag tag) {
        jdbcTemplate.update(UPDATE_TAG_SQL, tag.getName(), tag.getId());
        return jdbcTemplate.queryForObject(FIND_TAG_BY_ID_SQL, new Object[]{tag.getId()}, tagMapper);
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update(REMOVE_TAG_BY_ID_SQL, id);
    }

    @Override
    public void removeTagFromCertificates(long id) {
        jdbcTemplate.update(REMOVE_TAG_FROM_CERTIFICATES_BY_ID_SQL, id);
    }

    @Override
    public Tag findWidelyUsedTag() {
        long widelyUsedTagId = jdbcTemplate.queryForObject(FIND_WIDELY_USED_TAG, Long.class);
        return jdbcTemplate.queryForObject(FIND_TAG_BY_ID_SQL, new Object[]{widelyUsedTagId}, tagMapper);
    }
}
