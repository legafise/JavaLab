package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Profile({"template", "template-test"})
public class TagMapperImpl implements RowMapper<Tag> {
    private static final String TAG_ID = "tag_id";
    private static final String TAG_NAME = "tag_name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(TAG_ID));
        tag.setName(rs.getString(TAG_NAME));
        return tag;
    }
}
