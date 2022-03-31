package com.epam.esm.service.handler;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.InvalidSortParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

class CertificatesHandlerTest {
    private Certificate firstTestCertificate;
    private Certificate secondTestCertificate;
    private Certificate thirdTestCertificate;
    private List<Certificate> certificates;

    @BeforeEach
    void setUp() {
        Tag firstTestTag = new Tag(1, "Jumps");
        Tag secondTestTag = new Tag(2, "Fly");
        Tag thirdTestTag = new Tag(3, "Entertainment");

        firstTestCertificate = new Certificate(1, "Jump park", "Free jumps for your health!",
                new BigDecimal("30"), (short) 30, LocalDateTime.of(2021, 1, 10, 13, 5, 7),
                LocalDateTime.of(2021, 1, 10, 13, 5, 7), new HashSet<>(Arrays.asList(firstTestTag, thirdTestTag)));
        secondTestCertificate = new Certificate(2, "Fly tube", "Flying in air tube!",
                new BigDecimal("70"), (short) 30, LocalDateTime.of(2022, 2, 15, 11, 6, 23),
                LocalDateTime.of(2022, 2, 15, 11, 6, 23), new HashSet<>(Arrays.asList(secondTestTag, thirdTestTag)));
        thirdTestCertificate = new Certificate(3, "Casino park", "Free 100 spins!",
                new BigDecimal("100"), (short) 60, LocalDateTime.of(2021, 12, 30, 5, 37, 48),
                LocalDateTime.of(2021, 12, 30, 5, 37, 48), new HashSet<>(Collections.singletonList(thirdTestTag)));

        certificates = Arrays.asList(firstTestCertificate, secondTestCertificate, thirdTestCertificate);
    }

    @Test
    void findByNamePartTest() {
        Assertions.assertEquals(Arrays.asList(firstTestCertificate, thirdTestCertificate), CertificatesHandler.FIND_BY_NAME_PART.handle(certificates, "park"));
    }

    @Test
    void findByUnknownNamePartTest() {
        Assertions.assertEquals(new ArrayList<>(), CertificatesHandler.FIND_BY_NAME_PART.handle(certificates, "test"));
    }

    @Test
    void findByDescriptionPartTest() {
        Assertions.assertEquals(Arrays.asList(firstTestCertificate, thirdTestCertificate), CertificatesHandler.FIND_BY_DESCRIPTION_PART.handle(certificates, "free"));
    }

    @Test
    void findByUnknownDescriptionPartTest() {
        Assertions.assertEquals(new ArrayList<>(), CertificatesHandler.FIND_BY_DESCRIPTION_PART.handle(certificates, "test"));
    }

    @Test
    void sortByNameAscTest() {
        Assertions.assertEquals(Arrays.asList(thirdTestCertificate, secondTestCertificate, firstTestCertificate), CertificatesHandler.SORT_BY_NAME.handle(certificates, "asc"));
    }

    @Test
    void sortByNameDescTest() {
        Assertions.assertEquals(Arrays.asList(firstTestCertificate, secondTestCertificate, thirdTestCertificate), CertificatesHandler.SORT_BY_NAME.handle(certificates, "desc"));
    }

    @Test
    void sortByNameWithInvalidParameterTest() {
        Assertions.assertThrows(InvalidSortParameterException.class,() -> CertificatesHandler.SORT_BY_NAME.handle(certificates, "apple"));
    }

    @Test
    void sortByCreateDateAscTest() {
        Assertions.assertEquals(Arrays.asList(firstTestCertificate, thirdTestCertificate, secondTestCertificate), CertificatesHandler.SORT_BY_CREATE_DATE.handle(certificates, "asc"));
    }

    @Test
    void sortByCreateDateDescTest() {
        Assertions.assertEquals(Arrays.asList(secondTestCertificate, thirdTestCertificate, firstTestCertificate), CertificatesHandler.SORT_BY_CREATE_DATE.handle(certificates, "desc"));
    }

    @Test
    void sortByCreateDateWithInvalidParameterTest() {
        Assertions.assertThrows(InvalidSortParameterException.class,() -> CertificatesHandler.SORT_BY_CREATE_DATE.handle(certificates, "carrot"));
    }

    @Test
    void sortByLastUpdateDateAscTest() {
        Assertions.assertEquals(Arrays.asList(firstTestCertificate, thirdTestCertificate, secondTestCertificate), CertificatesHandler.SORT_BY_LAST_UPDATE_DATE.handle(certificates, "asc"));
    }

    @Test
    void sortByLastUpdateDateDescTest() {
        Assertions.assertEquals(Arrays.asList(secondTestCertificate, thirdTestCertificate, firstTestCertificate), CertificatesHandler.SORT_BY_LAST_UPDATE_DATE.handle(certificates, "desc"));
    }

    @Test
    void sortByLastUpdateDateWithInvalidParameterTest() {
        Assertions.assertThrows(InvalidSortParameterException.class,() -> CertificatesHandler.SORT_BY_LAST_UPDATE_DATE.handle(certificates, "pear"));
    }
}