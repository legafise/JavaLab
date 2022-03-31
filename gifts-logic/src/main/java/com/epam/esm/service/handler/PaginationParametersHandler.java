package com.epam.esm.service.handler;

import java.util.Map;

public interface PaginationParametersHandler {
    Map<String, Integer> handlePaginationParameters(Map<String, String> paginationParameters);
}
