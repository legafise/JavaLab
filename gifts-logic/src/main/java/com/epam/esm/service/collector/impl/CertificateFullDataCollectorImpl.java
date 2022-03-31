package com.epam.esm.service.collector.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.collector.CertificateFullDataCollector;
import org.springframework.stereotype.Component;

@Component
public class CertificateFullDataCollectorImpl implements CertificateFullDataCollector {
    @Override
    public Certificate collectFullCertificateData(Certificate certificateWithUpdate, Certificate actualCertificate) {
        addCertificateName(certificateWithUpdate, actualCertificate);
        addCertificateDescription(certificateWithUpdate, actualCertificate);
        addCertificateDuration(certificateWithUpdate, actualCertificate);
        addCertificatePrice(certificateWithUpdate, actualCertificate);
        addCertificateCreateDate(certificateWithUpdate, actualCertificate);
        addCertificateLastUpdateDate(certificateWithUpdate, actualCertificate);
        addCertificateTags(certificateWithUpdate, actualCertificate);

        return certificateWithUpdate;
    }

    private void addCertificateName(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getName() == null) {
            certificateWithUpdate.setName(actualCertificate.getName());
        }
    }

    private void addCertificateDescription(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getDescription() == null) {
            certificateWithUpdate.setDescription(actualCertificate.getDescription());
        }
    }

    private void addCertificateDuration(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getDuration() == 0) {
            certificateWithUpdate.setDuration(actualCertificate.getDuration());
        }
    }

    private void addCertificatePrice(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getPrice() == null) {
            certificateWithUpdate.setPrice(actualCertificate.getPrice());
        }
    }

    private void addCertificateCreateDate(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getCreateDate() == null) {
            certificateWithUpdate.setCreateDate(actualCertificate.getCreateDate());
        }
    }

    private void addCertificateLastUpdateDate(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getLastUpdateDate() == null) {
            certificateWithUpdate.setLastUpdateDate(actualCertificate.getLastUpdateDate());
        }
    }

    private void addCertificateTags(Certificate certificateWithUpdate, Certificate actualCertificate) {
        if (certificateWithUpdate.getTags().isEmpty()) {
            certificateWithUpdate.setTags(actualCertificate.getTags());
        }
    }
}
