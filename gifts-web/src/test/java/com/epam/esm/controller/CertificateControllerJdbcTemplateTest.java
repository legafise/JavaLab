package com.epam.esm.controller;

import com.epam.esm.MJCApplication;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MJCApplication.class)
@ActiveProfiles("template-test")
@Transactional
class CertificateControllerJdbcTemplateTest {
    @Autowired
    private CertificateController certificateController;
    private Certificate testCertificate;
    private EntityModel<Certificate> testCertificateModel;
    private List<EntityModel<Certificate>> testCertificateModelList;

    @BeforeEach
    void setUp() {
        Tag firstTestTag = new Tag(102, "Jumps");
        Tag secondTestTag = new Tag(103, "Entertainment");
        Tag thirdTestTag = new Tag(101, "Tattoo");

        testCertificate = new Certificate(101, "TattooLand", "The certificate allows to you make a tattoo",
                new BigDecimal("125.00"), (short) 92, LocalDateTime.parse("2022-01-20T21:00"),
                LocalDateTime.parse("2022-04-20T21:00"), new HashSet<>(Collections.singletonList(thirdTestTag)));
        testCertificateModel = EntityModel.of(testCertificate);
        testCertificateModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).readTag(101)).withRel("Tag(Tattoo) information"));

        Certificate secondTestCertificate = new Certificate(102, "Jump park", "Free jumps at trampolines",
                new BigDecimal("35.00"), (short) 30, LocalDateTime.parse("2022-03-15T21:30"),
                LocalDateTime.parse("2022-06-15T21:30"), new HashSet<>(Arrays.asList(firstTestTag, secondTestTag)));
        EntityModel<Certificate> secondTestCertificateModel = EntityModel.of(secondTestCertificate);
        secondTestCertificateModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).readTag(102)).withRel("Tag(Jumps) information"));
        secondTestCertificateModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).readTag(103)).withRel("Tag(Entertainment) information"));

        testCertificateModelList = Arrays.asList(testCertificateModel, secondTestCertificateModel);
    }

    @Test
    void readCertificateByIdTest() {
        Assertions.assertEquals(testCertificateModel, certificateController.readCertificateById(101));
    }

    @Test
    void readCertificateWithInvalidIdTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> certificateController.readCertificateById(458));
    }

    @Test
    void readAllCertificatesWithInvalidPageTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "-10");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "2");

        Assertions.assertThrows(InvalidPaginationDataException.class, () -> certificateController.readAllCertificates(pageParameters, new ArrayList<>()));
    }

    @Test
    void readAllCertificatesWithInvalidPageSizeTest() {
        Map<String, String> pageParameters = new HashMap<>();
        pageParameters.put(PaginationConstant.PAGE_PARAMETER, "1");
        pageParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, "-35");

        Assertions.assertThrows(InvalidPaginationDataException.class, () -> certificateController.readAllCertificates(pageParameters, new ArrayList<>()));
    }

    @Test
    void readAllCertificatesWithoutPaginationParametersTest() {
        Assertions.assertThrows(MissingPageNumberException.class, () -> certificateController.readAllCertificates(new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void updateCertificateTest() {
        testCertificate.setName("SuperTattoo");
        EntityModel<Certificate> updatedCertificate = certificateController.updateCertificate(testCertificate, 101);
        testCertificate.setLastUpdateDate(updatedCertificate.getContent().getLastUpdateDate());
        testCertificate.setTags(updatedCertificate.getContent().getTags());
        Assertions.assertEquals(testCertificateModel, updatedCertificate);
    }

    @Test
    void updateCertificateWithInvalidCertificateIdTest() {
        testCertificate.setName("SuperTattoo");
        Assertions.assertThrows(UnknownEntityException.class, () -> certificateController.updateCertificate(testCertificate, 294));
    }

    @Test
    void updateInvalidCertificateTest() {
        testCertificate.setName("");
        Assertions.assertThrows(InvalidEntityException.class, () -> certificateController.updateCertificate(testCertificate, 101));
    }

    @Test
    void createInvalidCertificateTest() {
        testCertificate.setName("");
        Assertions.assertThrows(InvalidEntityException.class, () -> certificateController.createCertificate(testCertificate));
    }

    @Test
    void createExistedCertificateTest() {
        Assertions.assertThrows(EntityDuplicationException.class, () -> certificateController.createCertificate(testCertificate));
    }

    @Test
    void patchCertificateTest() {
        Certificate certificate = new Certificate();
        certificate.setName("NewCertificate");
        testCertificate.setName("NewCertificate");
        EntityModel<Certificate> patchedCertificateModel = certificateController.patchCertificate(certificate, 101);
        testCertificate.setLastUpdateDate(patchedCertificateModel.getContent().getLastUpdateDate());
        Assertions.assertEquals(testCertificateModel, patchedCertificateModel);
    }

    @Test
    void patchUnknownCertificateTest() {
        Certificate certificate = new Certificate();
        certificate.setName("NewCertificate");
        Assertions.assertThrows(UnknownEntityException.class, () -> certificateController.patchCertificate(certificate, 837));
    }

    @Test
    void patchDuplicateCertificateTest() {
        Certificate certificate = new Certificate();
        certificate.setName("Jump park");
        Assertions.assertThrows(EntityDuplicationException.class, () -> certificateController.patchCertificate(certificate, 101));
    }

    @Test
    void deleteCertificateTest() {
        certificateController.deleteCertificate(101);
    }

    @Test
    void deleteUnknownCertificateTest() {
        Assertions.assertThrows(UnknownEntityException.class, () -> certificateController.deleteCertificate(3775));
    }
}