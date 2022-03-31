package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private static final String TAG_INFO_MESSAGE = "Tag(%s) information";
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<EntityModel<Certificate>> readAllCertificates(@RequestParam Map<String, String> parameters,
                                                              @RequestParam(required = false) List<String> tagNames) {
        parameters.remove("tagNames");
        List<Certificate> readCertificates = certificateService.findAllCertificates(parameters, tagNames);

        return readCertificates.stream()
                .map(this::createHateoasCertificateModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public EntityModel<Certificate> readCertificateById(@PathVariable long id) {
        Certificate readCertificate = certificateService.findCertificateById(id);

        return createHateoasCertificateModel(readCertificate);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public EntityModel<Certificate> createCertificate(@RequestBody Certificate certificate) {
        Certificate createdCertificate = certificateService.addCertificate(certificate);
        return createHateoasCertificateModel(createdCertificate);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public EntityModel<Certificate> updateCertificate(@RequestBody Certificate certificate, @PathVariable long id) {
        certificate.setId(id);
        Certificate updatedCertificate = certificateService.updateCertificate(certificate);
        return createHateoasCertificateModel(updatedCertificate);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public EntityModel<Certificate> patchCertificate(@RequestBody Certificate certificate, @PathVariable long id) {
        certificate.setId(id);
        Certificate patchedCertificate = certificateService.patchCertificate(certificate);
        return createHateoasCertificateModel(patchedCertificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCertificate(@PathVariable long id) {
        certificateService.removeCertificateById(id);
    }

    private EntityModel<Certificate> createHateoasCertificateModel(Certificate certificate) {
        EntityModel<Certificate> certificateModel = EntityModel.of(certificate);
        certificate.getTags().forEach(tag -> {
            WebMvcLinkBuilder linkToTag = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                    .readTag(tag.getId()));
            certificateModel.add(linkToTag.withRel(String.format(TAG_INFO_MESSAGE, tag.getName())));
        });

        return certificateModel;
    }
}