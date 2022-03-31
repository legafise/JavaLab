package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.validator.CertificateValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CertificateValidatorImpl implements CertificateValidator {
    private static final String CERTIFICATE_IS_NULL_MESSAGE = "certificate is null";
    private static final BigDecimal MIN_PRICE = new BigDecimal("1");
    private static final BigDecimal MAX_PRICE = new BigDecimal("100000");
    private static final long MIN_DURATION = 7;
    private static final long MAX_DURATION = 365;
    private List<String> invalidValues;

    public CertificateValidatorImpl() {
        invalidValues = new ArrayList<>();
    }

    @Override
    public void validateCertificate(Certificate certificate) {
        if (certificate == null) {
            throw new InvalidEntityException(Certificate.class, CERTIFICATE_IS_NULL_MESSAGE);
        }

        validateName(certificate.getName());
        validateDescription(certificate.getDescription());
        validatePrice(certificate.getPrice());
        validateDuration(certificate.getDuration());
        validateCreateDate(certificate.getCreateDate());
        validateLastUpdateDate(certificate.getLastUpdateDate());
        validateTags(certificate.getTags());

        if (!invalidValues.isEmpty()) {
            throw new InvalidEntityException(Certificate.class, collectCause(invalidValues));
        }
    }

    private String collectCause(List<String> invalidParams) {
        StringBuilder cause = new StringBuilder();
        for (int i = 0; i < invalidParams.size(); i++) {
            if (i < invalidParams.size() - 1) {
                cause.append(invalidParams.get(i)).append(", ");
            } else {
                cause.append(invalidParams.get(i));
            }
        }

        invalidValues = new ArrayList<>();
        return cause.toString();
    }

    private void validateTags(Set<Tag> tags) {
        if (tags == null) {
            invalidValues.add("tags is null");
        }
    }

    private void validateCreateDate(LocalDateTime createDate) {
        if (createDate == null) {
            invalidValues.add("create date");
        }
    }

    private void validateLastUpdateDate(LocalDateTime createDate) {
        if (createDate == null) {
            invalidValues.add("last update date");
        }
    }

    private void validateDuration(short duration) {
        if (!(duration > MIN_DURATION && duration <= MAX_DURATION)) {
            invalidValues.add("duration");
        }
    }

    private void validateName(String certificateName) {
        if (!(certificateName != null && certificateName.length() > 1
                && certificateName.length() <= 100)) {
            invalidValues.add("name");
        }
    }

    private void validateDescription(String certificateDescription) {
        if (!(certificateDescription != null && certificateDescription.length() >= 15 && certificateDescription.length() <= 500)) {
            invalidValues.add("description");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (!(price != null && price.compareTo(MAX_PRICE) < 1 && price.compareTo(MIN_PRICE) > -1)) {
            invalidValues.add("price");
        }
    }
}
