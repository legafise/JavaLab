package com.epam.esm.dao.impl;

import com.epam.esm.TestApplication;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles({"hibernate-test"})
@Transactional
class HibernateCertificateDaoTest {
    @Autowired
    private HibernateCertificateDao certificateDao;
    private Certificate testCertificate;
    private Certificate firstTestCertificate;
    private Certificate secondTestCertificate;

    @BeforeEach
    void setUp() {
        Tag firstTestTag = new Tag(102, "Jumps");
        Tag secondTestTag = new Tag(103, "Entertainment");
        Tag thirdTestTag = new Tag(101, "Tattoo");

        testCertificate = new Certificate(1, "Test", "Test certificate",
                new BigDecimal("100.00"), (short) 61, LocalDateTime.parse("2022-03-15T21:30"),
                LocalDateTime.parse("2022-06-15T21:30"), Collections.emptySet());
        firstTestCertificate = new Certificate(102, "Jump park", "Free jumps at trampolines",
                new BigDecimal("35.00"), (short) 30, LocalDateTime.parse("2022-03-15T21:30"),
                LocalDateTime.parse("2022-06-15T21:30"), new HashSet<>(Arrays.asList(firstTestTag, secondTestTag)));
        secondTestCertificate = new Certificate(101, "TattooLand", "The certificate allows to you make a tattoo",
                new BigDecimal("125.00"), (short) 92, LocalDateTime.parse("2022-03-15T21:30"),
                LocalDateTime.parse("2022-06-15T21:30"), new HashSet<>(Collections.singletonList(thirdTestTag)));
    }

    @Test
    void findCertificateByIdTest() {
        Assertions.assertEquals(certificateDao.findById(102).get(), firstTestCertificate);
    }

    @Test
    void findCertificateByNameTest() {
        Assertions.assertEquals(certificateDao.findByName("Jump park").get(), firstTestCertificate);
    }

    @Test
    void updateCertificateTest() {
        secondTestCertificate.setName("Tattoo");
        Assertions.assertEquals(secondTestCertificate, certificateDao.update(secondTestCertificate));
    }

    @Test
    void removeCertificateTest() {
        certificateDao.remove(103);
        Assertions.assertFalse(certificateDao.findById(103).isPresent());
    }

    @Test
    void addTagToCertificateTest() {
        Assertions.assertTrue(certificateDao.addTagToCertificate(101, 103));
    }

    @Test
    void clearCertificateTagsTest() {
        Assertions.assertTrue(certificateDao.clearCertificateTags(101));
    }

    @Test
    void findMaxCertificateIdTest() {
        Assertions.assertEquals(103, certificateDao.findMaxCertificateId());
    }
}