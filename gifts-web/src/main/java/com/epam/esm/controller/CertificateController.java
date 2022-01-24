package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Certificate> readAllCertificates() {
        return certificateService.findAllCertificates();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Certificate createCertificate(@RequestBody Certificate certificate) {
        return certificateService.addCertificate(certificate);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(OK)
    public Certificate readCertificateById(@PathVariable String id) {
        return certificateService.findCertificateById(id);
    }

    @GetMapping("/tag/{tag}")
    @ResponseStatus(OK)
    public List<Certificate> readCertificatesByTagName(@PathVariable String tag) {
        return certificateService.findCertificatesByTagName(tag);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(OK)
    public List<Certificate> readCertificatesByNamePart(@PathVariable String name) {
        return certificateService.findCertificatesByNamePart(name);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public Certificate updateCertificate(@RequestBody Certificate certificate, @PathVariable String id) {
        return certificateService.updateCertificate(certificate, id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public Certificate patchCertificate(@RequestBody Certificate certificate, @PathVariable String id) {
        return certificateService.patchCertificate(certificate, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCertificate(@PathVariable String id) {
        certificateService.removeCertificateById(id);
    }
}
