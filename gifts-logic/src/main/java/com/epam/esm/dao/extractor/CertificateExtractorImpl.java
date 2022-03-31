package com.epam.esm.dao.extractor;

import com.epam.esm.dao.mapper.TagMapperImpl;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile({"template", "template-test"})
public class CertificateExtractorImpl implements ResultSetExtractor<List<Certificate>> {
    private static final String CERTIFICATE_ID = "certificate_id";
    private static final String GIFT_CERTIFICATE_NAME = "gift_certificate_name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "certificate_price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final String IS_DELETED = "is_deleted";
    private final TagMapperImpl tagMapper;

    @Autowired
    public CertificateExtractorImpl(TagMapperImpl tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<Certificate> extractData(ResultSet resultSet) throws DataAccessException, SQLException {
        List<Certificate> certificateList = new ArrayList<>();

        while (resultSet.next()) {
            Certificate certificate = new Certificate();
            fillMainCertificateData(certificate, resultSet);
            certificate.setTags(mapCertificateTags(resultSet));

            certificateList.add(certificate);
        }

        return certificateList;
    }


    public Certificate extractUserOrderCertificate(ResultSet resultSet, long mappingOrderId) throws SQLException {
        Certificate certificate = new Certificate();
        fillMainCertificateData(certificate, resultSet);
        certificate.setTags(new HashSet<>());
        Set<Tag> tags = new HashSet<>();

        while (!resultSet.isAfterLast() && resultSet.getLong("order_id") == mappingOrderId) {
            Tag tag = tagMapper.mapRow(resultSet, 1);
            if (tag == null || tag.getName() == null) {
                return certificate;
            }

            tags.add(tag);
            resultSet.next();
        }

        resultSet.previous();
        certificate.setTags(tags);
        return certificate;
    }

    private Set<Tag> mapCertificateTags(ResultSet resultSet) throws SQLException {
        Set<Tag> tagSet = new HashSet<>();
        long mappingCertificateId = resultSet.getLong(CERTIFICATE_ID);

        while (!resultSet.isAfterLast()) {
            if (resultSet.getLong(CERTIFICATE_ID) == mappingCertificateId) {
                tagSet.add(tagMapper.mapRow(resultSet, 1));
            } else if (resultSet.getLong(CERTIFICATE_ID) != mappingCertificateId) {
                resultSet.previous();
                return tagSetChecker(tagSet);
            }

            resultSet.next();
        }

        return tagSetChecker(tagSet);
    }

    private Set<Tag> tagSetChecker(Set<Tag> tagSet) {
        return tagSet.size() == 1 && tagSet.stream()
                .findFirst().get().getName() == null ? new HashSet<>() : tagSet;
    }

    private void fillMainCertificateData(Certificate certificate, ResultSet resultSet) throws SQLException {
        certificate.setId(resultSet.getLong(CERTIFICATE_ID));
        certificate.setName(resultSet.getString(GIFT_CERTIFICATE_NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getBigDecimal(PRICE));
        certificate.setDuration(resultSet.getShort(DURATION));
        certificate.setCreateDate(resultSet.getTimestamp(CREATE_DATE).toLocalDateTime());
        certificate.setLastUpdateDate(resultSet.getTimestamp(LAST_UPDATE_DATE).toLocalDateTime());
        certificate.setDeleted(resultSet.getBoolean(IS_DELETED));
    }
}

