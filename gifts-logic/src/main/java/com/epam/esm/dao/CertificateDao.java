package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {
    void add(Certificate certificate);

    Optional<Certificate> findById(long id);

    List<Certificate> findAll(int page, int pageSize);

    Certificate update(Certificate certificate);

    void remove(long id);

    boolean addTagToCertificate(long certificateId, long tagId);

    boolean clearCertificateTags(long certificateId);

    Optional<Certificate> findByName(String name);

    long findMaxCertificateId();
}
