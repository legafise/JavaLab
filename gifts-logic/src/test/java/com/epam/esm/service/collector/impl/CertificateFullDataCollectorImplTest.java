package com.epam.esm.service.collector.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.collector.CertificateFullDataCollector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

class CertificateFullDataCollectorImplTest {
    private CertificateFullDataCollector certificateFullDataCollector;
    private Certificate firstTestCertificate;

    @BeforeEach
    void setUp() {
        certificateFullDataCollector = new CertificateFullDataCollectorImpl();
        Tag firstTestTag = new Tag(1, "Jumps");
        Tag secondTag = new Tag(2, "Entertainment");
        firstTestCertificate = new Certificate(2, "Jump park", "Free jumps for your health!",
                new BigDecimal("30"), (short) 30, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(firstTestTag, secondTag)));
    }

    @Test
    void collectFullCertificateDataPositiveTest() {
        Certificate testCertificate = new Certificate();
        testCertificate.setId(2);
        testCertificate.setName("TestCertificate");

        Certificate result = new Certificate(2, testCertificate.getName(), firstTestCertificate.getDescription(),
                firstTestCertificate.getPrice(), firstTestCertificate.getDuration(), firstTestCertificate.getCreateDate(),
                firstTestCertificate.getLastUpdateDate(), firstTestCertificate.getTags());

        Assertions.assertEquals(certificateFullDataCollector
                .collectFullCertificateData(testCertificate, firstTestCertificate), result);
    }
}