package com.epam.esm.service.handler.impl;

import com.epam.esm.service.exception.InvalidPaginationDataException;
import com.epam.esm.service.exception.MissingPageNumberException;
import com.epam.esm.service.handler.PaginationParametersHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.service.constant.PaginationConstant.PAGE_PARAMETER;
import static com.epam.esm.service.constant.PaginationConstant.PAGE_SIZE_PARAMETER;

@Component
public class PaginationParametersHandlerImpl implements PaginationParametersHandler {
    private static final String MISSING_PAGE_NUMBER_MESSAGE = "missing.page.number";
    private static final String INVALID_PAGE_OR_PAGE_SIZE_MESSAGE = "invalid.page.or.page.size";
    private static final int DEFAULT_PAE_SIZE = 5;

    @Override
    public Map<String, Integer> handlePaginationParameters(Map<String, String> paginationParameters) {
        if (paginationParameters == null || paginationParameters.isEmpty() || !paginationParameters.containsKey(PAGE_PARAMETER)) {
            throw new MissingPageNumberException(MISSING_PAGE_NUMBER_MESSAGE);
        }

        int page = Integer.parseInt(paginationParameters.remove(PAGE_PARAMETER));
        int pageSize = paginationParameters.containsKey(PAGE_SIZE_PARAMETER)
                ? Integer.parseInt(paginationParameters.remove(PAGE_SIZE_PARAMETER))
                : DEFAULT_PAE_SIZE;

        if (page <= 0 || pageSize <= 0) {
            throw new InvalidPaginationDataException(INVALID_PAGE_OR_PAGE_SIZE_MESSAGE);
        }

        HashMap<String, Integer> handledPaginationParameters = new HashMap<>();
        handledPaginationParameters.put(PAGE_PARAMETER, page);
        handledPaginationParameters.put(PAGE_SIZE_PARAMETER, pageSize);
        return handledPaginationParameters;
    }
}
