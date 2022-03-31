package com.epam.esm.service;

import com.epam.esm.entity.Certificate;

import java.util.List;
import java.util.Map;

public interface CertificateService {
    Certificate addCertificate(Certificate certificate);

    List<Certificate> findAllCertificates(Map<String, String> parameters, List<String> tagNames);

    Certificate findCertificateById(long id);

    Certificate updateCertificate(Certificate certificate);

    void removeCertificateById(long id);

    Certificate patchCertificate(Certificate certificate);
}
