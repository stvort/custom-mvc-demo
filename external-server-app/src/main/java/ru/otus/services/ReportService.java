package ru.otus.services;

import ru.otus.dto.SalesDataDto;

import java.util.List;

public interface ReportService {
    List<SalesDataDto> prepareReport();

    boolean supports(String reportName);
}
