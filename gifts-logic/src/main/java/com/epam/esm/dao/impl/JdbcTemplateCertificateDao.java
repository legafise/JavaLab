package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.extractor.CertificateExtractorImpl;
import com.epam.esm.entity.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile({"template", "template-test"})
public class JdbcTemplateCertificateDao implements CertificateDao {
    private static final String ADD_CERTIFICATE_SQL = "INSERT INTO gift_certificates (name, description, price," +
            " duration, create_date, last_update_date, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String ADD_TAG_TO_CERTIFICATE_SQL = "INSERT INTO gift_tags (certificate_id, tag_id)" +
            " VALUES (?, ?)";
    private static final String CLEAR_CERTIFICATE_TAGS_SQL = "DELETE FROM gift_tags WHERE certificate_id = ?";
    private static final String UPDATE_CERTIFICATE_SQL = "UPDATE gift_certificates SET name = ?, description = ?, price = ?," +
            " duration = ?, create_date = ?, last_update_date = ?, is_deleted = ? WHERE id = ?";
    private static final String REMOVE_CERTIFICATE_BY_ID_SQL = "DELETE FROM gift_certificates WHERE id = ?";
    private static final String FIND_CERTIFICATE_BY_ID_SQL = "SELECT gift_certificates.id AS certificate_id," +
            " gift_certificates.name AS gift_certificate_name, gift_certificates.description, gift_certificates.price AS certificate_price," +
            " gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date, tags.id AS" +
            " tag_id, tags.name AS tag_name, gift_certificates.is_deleted FROM gift_certificates LEFT JOIN gift_tags" +
            " ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON gift_tags.tag_id = tags.id WHERE gift_certificates.id = ?";
    private static final String FIND_CERTIFICATE_BY_NAME_SQL = "SELECT gift_certificates.id AS certificate_id," +
            " gift_certificates.name AS gift_certificate_name, gift_certificates.description, gift_certificates.price AS certificate_price," +
            " gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date, tags.id AS" +
            " tag_id, tags.name AS tag_name, gift_certificates.is_deleted FROM gift_certificates LEFT JOIN gift_tags" +
            " ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON gift_tags.tag_id = tags.id WHERE gift_certificates.name = ?";
    private static final String FIND_ALL_CERTIFICATES_SQL = "SELECT gift_certificates.id AS certificate_id," +
            " gift_certificates.name AS gift_certificate_name, gift_certificates.description, gift_certificates.price AS certificate_price," +
            " gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date, tags.id AS" +
            " tag_id, tags.name AS tag_name, gift_certificates.is_deleted FROM gift_certificates LEFT JOIN gift_tags" +
            " ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON gift_tags.tag_id = tags.id ORDER BY" +
            " gift_certificates.id ASC LIMIT ? OFFSET ?";
    private static final String FIND_MAX_CERTIFICATE_ID = "SELECT MAX(id) FROM gift_certificates";
    private final CertificateExtractorImpl certificateExtractor;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateCertificateDao(CertificateExtractorImpl certificateExtractor, JdbcTemplate jdbcTemplate) {
        this.certificateExtractor = certificateExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Certificate certificate) {
        jdbcTemplate.update(ADD_CERTIFICATE_SQL, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), certificate.getCreateDate(),
                certificate.getLastUpdateDate(), certificate.isDeleted());
    }

    @Override
    public Optional<Certificate> findById(long id) {
        List<Certificate> certificateList = jdbcTemplate.query(FIND_CERTIFICATE_BY_ID_SQL, certificateExtractor, id);

        return certificateList == null || certificateList.isEmpty() ? Optional.empty()
                : Optional.of(certificateList.get(0));
    }

    @Override
    public List<Certificate> findAll(int page, int pageSize) {
        return jdbcTemplate.query(FIND_ALL_CERTIFICATES_SQL, new Object[]{pageSize, (page - 1) * pageSize}, certificateExtractor);
    }

    @Override
    public Certificate update(Certificate certificate) {
        jdbcTemplate.update(UPDATE_CERTIFICATE_SQL, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), certificate.getCreateDate(),
                certificate.getLastUpdateDate(), certificate.isDeleted(), certificate.getId());
        return Objects.requireNonNull(jdbcTemplate
                .query(FIND_CERTIFICATE_BY_ID_SQL, certificateExtractor, certificate.getId())).get(0);
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update(REMOVE_CERTIFICATE_BY_ID_SQL, id);
    }

    @Override
    public boolean addTagToCertificate(long certificateId, long tagId) {
        return 1 == jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE_SQL, certificateId, tagId);
    }

    @Override
    public boolean clearCertificateTags(long certificateId) {
        return 1 == jdbcTemplate.update(CLEAR_CERTIFICATE_TAGS_SQL, certificateId);
    }

    @Override
    public Optional<Certificate> findByName(String name) {
        List<Certificate> certificateList = jdbcTemplate.query(FIND_CERTIFICATE_BY_NAME_SQL, certificateExtractor, name);

        return certificateList == null || certificateList.isEmpty() ? Optional.empty()
                : Optional.of(certificateList.get(0));
    }

    @Override
    public long findMaxCertificateId() {
        return jdbcTemplate.queryForObject(FIND_MAX_CERTIFICATE_ID, Long.class);
    }
}
