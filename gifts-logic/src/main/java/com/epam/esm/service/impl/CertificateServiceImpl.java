package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.checker.CertificateDuplicationChecker;
import com.epam.esm.service.collector.CertificateFullDataCollector;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.UnknownEntityException;
import com.epam.esm.service.handler.CertificatesHandler;
import com.epam.esm.service.validator.CertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
    private static final String NONEXISTENT_CERTIFICATE_MESSAGE = "nonexistent.certificate";
    private static final String DUPLICATE_CERTIFICATE_MESSAGE = "duplicate.certificate";
    private final CertificateDao certificateDao;
    private final TagService tagService;
    private final CertificateValidator certificateValidator;
    private final CertificateDuplicationChecker certificateDuplicationChecker;
    private final CertificateFullDataCollector certificateFullDataCollector;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagService tagService, CertificateValidator certificateValidator, CertificateDuplicationChecker certificateDuplicationChecker, CertificateFullDataCollector certificateFullDataCollector) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
        this.certificateValidator = certificateValidator;
        this.certificateDuplicationChecker = certificateDuplicationChecker;
        this.certificateFullDataCollector = certificateFullDataCollector;
    }

    @Override
    @Transactional
    public Certificate addCertificate(Certificate certificate) {
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());

        certificateValidator.validateCertificate(certificate);
        if (!certificateDuplicationChecker.checkCertificateForAddingDuplication(certificate)) {
            throw new DuplicateEntityException(Certificate.class, DUPLICATE_CERTIFICATE_MESSAGE);
        }

        Set<Tag> tags = certificate.getTags();
        certificate.setTags(new HashSet<>());
        certificateDao.add(certificate);
        long addedCertificateId = certificateDao.findMaxCertificateId();
        addCertificateTags(addedCertificateId, tags);
        return findCertificateById(addedCertificateId);
    }

    @Override
    public List<Certificate> findAllCertificates(Map<String, String> handleParameters, List<String> tagNames) {
        List<Certificate> certificates = certificateDao.findAll();

        if (tagNames != null && !tagNames.isEmpty()) {
            List<Tag> tags = tagNames.stream()
                    .map(tagService::findTagByName)
                    .collect(Collectors.toList());
            certificates = certificates.stream()
                    .filter(certificate -> certificate.getTags().stream()
                            .anyMatch(tags::contains))
                    .collect(Collectors.toList());
        }

        if (handleParameters != null && !handleParameters.isEmpty()) {
            for (Map.Entry<String, String> parametersEntry : handleParameters.entrySet()) {
                certificates = CertificatesHandler
                        .findHandlerByName(parametersEntry.getKey())
                        .handle(certificates, parametersEntry.getValue());
            }
        }

        return certificates;
    }

    @Override
    public Certificate findCertificateById(long id) {
        Optional<Certificate> certificate = certificateDao.findById(id);
        if (!certificate.isPresent()) {
            throw new UnknownEntityException(Certificate.class, NONEXISTENT_CERTIFICATE_MESSAGE);
        }

        return certificate.get();
    }

    @Override
    @Transactional
    public Certificate updateCertificate(Certificate certificate) {
        Certificate actualCertificate = findCertificateById(certificate.getId());

        if (certificate.getTags().isEmpty()) {
            certificate.setTags(new HashSet<>(actualCertificate.getTags()));
        }
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setCreateDate(actualCertificate.getCreateDate());

        certificateValidator.validateCertificate(certificate);
        if (!certificateDuplicationChecker.checkCertificateForUpdatingDuplication(certificate)) {
            throw new DuplicateEntityException(Certificate.class, DUPLICATE_CERTIFICATE_MESSAGE);
        }

        Set<Tag> tags = certificate.getTags();
        certificate.setTags(new HashSet<>());
        certificateDao.update(certificate);
        addCertificateTags(certificate.getId(), tags);
        return findCertificateById(certificate.getId());
    }

    @Override
    @Transactional
    public void removeCertificateById(long id) {
        if (!certificateDao.findById(id).isPresent()) {
            throw new UnknownEntityException(Certificate.class, NONEXISTENT_CERTIFICATE_MESSAGE);
        }

        certificateDao.clearCertificateTags(id);
        certificateDao.remove(id);
    }

    @Override
    @Transactional
    public Certificate patchCertificate(Certificate certificate) {
        return updateCertificate(certificateFullDataCollector
                .collectFullCertificateData(certificate, findCertificateById(certificate.getId())));
    }

    public void addCertificateTags(long certificateId, Set<Tag> tags) {
        if (!tags.isEmpty()) {
            tags.forEach(tag -> {
                tagService.addTagIfNotExists(tag);
                Tag currentTag = tagService.findTagByName(tag.getName());
                certificateDao.addTagToCertificate(certificateId, currentTag.getId());
            });
        }
    }
}