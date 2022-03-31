package com.epam.esm.service.handler;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.exception.InvalidSortParameterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum CertificatesHandler {
    FIND_BY_NAME_PART("namePart") {
        @Override
        public List<Certificate> handle(List<Certificate> certificateList, String parameter) {
            return certificateList.stream()
                    .filter(currentCertificate -> currentCertificate.getName()
                            .toUpperCase().contains(parameter.toUpperCase()))
                    .collect(Collectors.toList());
        }
    },
    FIND_BY_DESCRIPTION_PART("descriptionPart") {
        @Override
        public List<Certificate> handle(List<Certificate> certificateList, String parameter) {
            return certificateList.stream()
                    .filter(currentCertificate -> currentCertificate.getDescription()
                            .toUpperCase().contains(parameter.toUpperCase()))
                    .collect(Collectors.toList());
        }
    },
    SORT_BY_NAME("nameSort") {
        @Override
        public List<Certificate> handle(List<Certificate> certificateList, String parameter) {
            List<Certificate> handledList = certificateList.stream()
                    .sorted(Comparator.comparing(firstCertificate -> firstCertificate.getName().toUpperCase()))
                    .collect(Collectors.toList());

            return defineListOrder(handledList, parameter);
        }
    },
    SORT_BY_CREATE_DATE("createDateSort") {
        @Override
        public List<Certificate> handle(List<Certificate> certificateList, String parameter) {
            List<Certificate> handledList = certificateList.stream()
                    .sorted(Comparator.comparing(Certificate::getCreateDate))
                    .collect(Collectors.toList());

           return defineListOrder(handledList, parameter);
        }
    },
    SORT_BY_LAST_UPDATE_DATE("lastUpdateDateSort") {
        @Override
        public List<Certificate> handle(List<Certificate> certificateList, String parameter) {
            List<Certificate> handledList = certificateList.stream()
                    .sorted(Comparator.comparing(Certificate::getLastUpdateDate))
                    .collect(Collectors.toList());

            return defineListOrder(handledList, parameter);
        }
    };

    private static final String ASC_PARAMETER = "ASC";
    private static final String DESC_PARAMETER = "DESC";
    private static final String INVALID_HANDLER_MESSAGE = "invalid.handler";
    private static final String INVALID_SORT_PARAMETER_MESSAGE = "invalid.sort.parameter";
    private final String handlerName;

    CertificatesHandler(String sortName) {
        this.handlerName = sortName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public abstract List<Certificate> handle(List<Certificate> certificateList, String searchParameter);

    public static CertificatesHandler findHandlerByName(String handlerTypeName) {
        return Arrays.stream(values())
                .filter(certificatesSortHandler -> certificatesSortHandler.getHandlerName()
                        .equalsIgnoreCase(handlerTypeName.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidSortParameterException(INVALID_HANDLER_MESSAGE));
    }

    private static List<Certificate> defineListOrder(List<Certificate> certificateList, String orderParameter) {
        String parameterInUpperCase = orderParameter.toUpperCase();
        if (!parameterInUpperCase.equals(ASC_PARAMETER) && !parameterInUpperCase.equals(DESC_PARAMETER)) {
            throw new InvalidSortParameterException(INVALID_SORT_PARAMETER_MESSAGE);
        }

       return orderParameter.equalsIgnoreCase(ASC_PARAMETER)
               ? certificateList
               : invertCertificateList(certificateList);
    }

    private static List<Certificate> invertCertificateList(List<Certificate> certificateList) {
        List<Certificate> invertedCertificateList = new ArrayList<>();
        for (int i = certificateList.size() - 1; i >= 0; i--) {
            invertedCertificateList.add(certificateList.get(i));
        }

        return invertedCertificateList;
    }
}