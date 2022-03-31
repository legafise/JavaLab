package com.epam.esm.service.checker.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CertificateDuplicationCheckerImplTest {
    private CertificateDuplicationCheckerImpl certificateDuplicationChecker;
    private CertificateDao certificateDao;
    private Certificate firstTestCertificate;
    private Certificate secondTestCertificate;

    @BeforeEach
    void setUp() {
        certificateDao = mock(CertificateDao.class);
        certificateDuplicationChecker = new CertificateDuplicationCheckerImpl(certificateDao);

        Tag firstTestTag = new Tag(1, "Jumps");
        Tag secondTestTag = new Tag(2, "Fly");
        Tag thirdTestTag = new Tag(3, "Entertainment");

        firstTestCertificate = new Certificate(2, "Jump park", "Free jumps for your health!",
                new BigDecimal("30"), (short) 30, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(firstTestTag, thirdTestTag)));
        secondTestCertificate = new Certificate(1, "Fly tube", "Free flying in air tube!",
                new BigDecimal("70"), (short) 30, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(secondTestTag, thirdTestTag)));
    }

    @Test
    void checkCertificateForAddingDuplicationPositiveTest() {
        when(certificateDao.findByName("Jump park")).thenReturn(Optional.empty());
        Assertions.assertTrue(certificateDuplicationChecker.checkCertificateForAddingDuplication(firstTestCertificate));
    }

    @Test
    void checkCertificateForAddingDuplicationBadTest() {
        when(certificateDao.findByName("Jump park")).thenReturn(Optional.of(firstTestCertificate));
        Assertions.assertFalse(certificateDuplicationChecker.checkCertificateForAddingDuplication(firstTestCertificate));
    }

    @Test
    void checkCertificateForUpdatingDuplicationPositiveTest() {
        when(certificateDao.findByName("Jump park")).thenReturn(Optional.empty());
        Assertions.assertTrue(certificateDuplicationChecker.checkCertificateForUpdatingDuplication(firstTestCertificate));
    }

    @Test
    void checkCertificateForUpdatingDuplicationWithUpdatedCertificateTest() {
        when(certificateDao.findByName("Jump park")).thenReturn(Optional.of(firstTestCertificate));
        Assertions.assertTrue(certificateDuplicationChecker.checkCertificateForUpdatingDuplication(firstTestCertificate));
    }

    @Test
    void checkCertificateForUpdatingDuplicationBadTest() {
        when(certificateDao.findByName("Jump park")).thenReturn(Optional.of(secondTestCertificate));
        Assertions.assertFalse(certificateDuplicationChecker.checkCertificateForUpdatingDuplication(firstTestCertificate));
    }
}