package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.checker.CertificateDuplicationChecker;
import com.epam.esm.service.collector.CertificateFullDataCollector;
import com.epam.esm.service.constant.PaginationConstant;
import com.epam.esm.service.exception.EntityDuplicationException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.UnknownEntityException;
import com.epam.esm.service.handler.PaginationParametersHandler;
import com.epam.esm.service.validator.CertificateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

class CertificateServiceImplTest {
    private CertificateServiceImpl certificateService;
    private CertificateValidator certificateValidator;
    private CertificateDuplicationChecker certificateDuplicationChecker;
    private CertificateDao certificateDao;
    private CertificateFullDataCollector certificateFullDataCollector;
    private PaginationParametersHandler paginationParametersHandler;
    private TagService tagService;
    private TagDao tagDao;
    private Certificate firstTestCertificate;
    private Certificate secondTestCertificate;
    private List<Certificate> certificates;

    @BeforeEach
    void setUp() {
        tagService = mock(TagService.class);
        certificateDao = mock(CertificateDao.class);
        certificateValidator = mock(CertificateValidator.class);
        certificateDuplicationChecker = mock(CertificateDuplicationChecker.class);
        certificateFullDataCollector = mock(CertificateFullDataCollector.class);
        paginationParametersHandler = mock(PaginationParametersHandler.class);
        tagDao = mock(TagDao.class);
        certificateService = new CertificateServiceImpl(certificateDao, tagDao, tagService, certificateValidator,
                certificateDuplicationChecker, certificateFullDataCollector, paginationParametersHandler);

        Tag firstTestTag = new Tag(1, "Jumps");
        Tag secondTestTag = new Tag(2, "Fly");
        Tag thirdTestTag = new Tag(3, "Entertainment");

        firstTestCertificate = new Certificate(2, "Jump park", "Free jumps for your health!",
                new BigDecimal("30"), (short) 30, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(firstTestTag, thirdTestTag)));
        secondTestCertificate = new Certificate(1, "Fly tube", "Free flying in air tube!",
                new BigDecimal("70"), (short) 30, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(secondTestTag, thirdTestTag)));
        Certificate thirdTestCertificate = new Certificate(666, "DeletedCertificate", "U cant read the certificate!",
                new BigDecimal("666"), (short) 13, LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(Arrays.asList(new Tag(666, "Satan"), new Tag(13, "Hell"))));
        thirdTestCertificate.setDeleted(true);

        certificates = Arrays.asList(firstTestCertificate, secondTestCertificate, thirdTestCertificate);
    }

    @Test
    void addCertificatePositiveTest() {
        doNothing().when(certificateValidator).validateCertificate(firstTestCertificate);
        when(certificateDuplicationChecker.checkCertificateForAddingDuplication(firstTestCertificate)).thenReturn(true);
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        when(certificateDao.findMaxCertificateId()).thenReturn(2L);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doNothing().when(spyCertificateService).addCertificateTags(isA(Long.class), isA(Set.class));

        Assertions.assertEquals(spyCertificateService.addCertificate(firstTestCertificate), firstTestCertificate);
    }

    @Test
    void addInvalidCertificateTest() {
        doThrow(InvalidEntityException.class).when(certificateValidator).validateCertificate(firstTestCertificate);
        when(certificateDuplicationChecker.checkCertificateForAddingDuplication(firstTestCertificate)).thenReturn(true);
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        when(certificateDao.findMaxCertificateId()).thenReturn(2L);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doNothing().when(spyCertificateService).addCertificateTags(isA(Long.class), isA(Set.class));

        Assertions.assertThrows(InvalidEntityException.class, () -> spyCertificateService.addCertificate(firstTestCertificate));
    }

    @Test
    void addDuplicateCertificateTest() {
        doNothing().when(certificateValidator).validateCertificate(firstTestCertificate);
        when(certificateDuplicationChecker.checkCertificateForAddingDuplication(firstTestCertificate)).thenReturn(false);
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doNothing().when(spyCertificateService).addCertificateTags(isA(Long.class), isA(Set.class));

        Assertions.assertThrows(EntityDuplicationException.class, () -> spyCertificateService.addCertificate(firstTestCertificate));
    }

    @Test
    void findAllCertificatesWithoutParametersTest() {
        Map<String, String> parameters = new HashMap<>();

        Map<String, Integer> handledPaginationParameters = new HashMap<>();
        handledPaginationParameters.put(PaginationConstant.PAGE_PARAMETER, 1);
        handledPaginationParameters.put(PaginationConstant.PAGE_SIZE_PARAMETER, 2);

        when(certificateDao.findAll(1, 2)).thenReturn(certificates);
        when(paginationParametersHandler.handlePaginationParameters(parameters)).thenReturn(handledPaginationParameters);
        Assertions.assertEquals(certificateService.findAllCertificates(parameters, new ArrayList<>()), Arrays.asList(firstTestCertificate, secondTestCertificate));
    }

    @Test
    void findCertificateByIdPositiveTest() {
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        Assertions.assertEquals(certificateService.findCertificateById(2), firstTestCertificate);
    }

    @Test
    void findUnknownCertificateByIdTest() {
        when(certificateDao.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThrows(UnknownEntityException.class, () -> certificateService.findCertificateById(2));
    }

    @Test
    void removeNonExistCertificateTest() {
        when(certificateDao.findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(UnknownEntityException.class, () -> certificateService.removeCertificateById(2));
    }

    @Test
    void updateCertificatePositiveTest() {
        doNothing().when(certificateValidator).validateCertificate(firstTestCertificate);
        when(certificateDuplicationChecker.checkCertificateForUpdatingDuplication(firstTestCertificate)).thenReturn(true);
        when(certificateDao.update(firstTestCertificate)).thenReturn(firstTestCertificate);
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        when(certificateDao.clearCertificateTags(2)).thenReturn(true);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doNothing().when(spyCertificateService).addCertificateTags(isA(Long.class), isA(Set.class));
        Assertions.assertEquals(spyCertificateService.updateCertificate(firstTestCertificate), firstTestCertificate);
    }

    @Test
    void updateInvalidCertificateTest() {
        doThrow(InvalidEntityException.class).when(certificateValidator).validateCertificate(firstTestCertificate);
        when(certificateDuplicationChecker.checkCertificateForUpdatingDuplication(firstTestCertificate)).thenReturn(true);
        when(certificateDao.update(firstTestCertificate)).thenReturn(firstTestCertificate);
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        when(certificateDao.clearCertificateTags(2)).thenReturn(true);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doNothing().when(spyCertificateService).addCertificateTags(isA(Long.class), isA(Set.class));
        Assertions.assertThrows(InvalidEntityException.class, () -> spyCertificateService.updateCertificate(firstTestCertificate));
    }

    @Test
    void updateDuplicateCertificateTest() {
        doNothing().when(certificateValidator).validateCertificate(firstTestCertificate);
        when(certificateDuplicationChecker.checkCertificateForUpdatingDuplication(firstTestCertificate)).thenReturn(false);
        when(certificateDao.update(firstTestCertificate)).thenReturn(firstTestCertificate);
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        when(certificateDao.clearCertificateTags(2)).thenReturn(true);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doNothing().when(spyCertificateService).addCertificateTags(isA(Long.class), isA(Set.class));
        Assertions.assertThrows(EntityDuplicationException.class, () -> spyCertificateService.updateCertificate(firstTestCertificate));
    }

    @Test
    void patchCertificatePositiveTest() {
        Certificate testCertificate = new Certificate();
        testCertificate.setId(2);
        testCertificate.setName("TestCertificate");

        Certificate result = new Certificate(2, testCertificate.getName(), firstTestCertificate.getDescription(),
                firstTestCertificate.getPrice(), firstTestCertificate.getDuration(), firstTestCertificate.getCreateDate(),
                firstTestCertificate.getLastUpdateDate(), firstTestCertificate.getTags());
        when(certificateDao.findById(2)).thenReturn(Optional.of(firstTestCertificate));
        when(certificateFullDataCollector.collectFullCertificateData(testCertificate, firstTestCertificate)).thenReturn(result);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doReturn(result).when(spyCertificateService).updateCertificate(result);
        Assertions.assertEquals(spyCertificateService.patchCertificate(testCertificate), result);
    }

    @Test
    void patchUnknownCertificateTest() {
        Certificate testCertificate = new Certificate();
        testCertificate.setId(2);
        testCertificate.setName("TestCertificate");

        Certificate result = new Certificate(2, testCertificate.getName(), firstTestCertificate.getDescription(),
                firstTestCertificate.getPrice(), firstTestCertificate.getDuration(), firstTestCertificate.getCreateDate(),
                firstTestCertificate.getLastUpdateDate(), firstTestCertificate.getTags());
        when(certificateDao.findById(2)).thenReturn(Optional.empty());
        when(certificateFullDataCollector.collectFullCertificateData(testCertificate, firstTestCertificate)).thenReturn(result);
        CertificateServiceImpl spyCertificateService = Mockito.spy(certificateService);
        doReturn(result).when(spyCertificateService).updateCertificate(result);
        Assertions.assertThrows(UnknownEntityException.class, () -> spyCertificateService.patchCertificate(testCertificate));
    }
}