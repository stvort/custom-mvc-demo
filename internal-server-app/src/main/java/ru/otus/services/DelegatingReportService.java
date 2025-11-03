package ru.otus.services;

import lombok.RequiredArgsConstructor;
import ru.otus.dto.SalesDataDto;
import ru.otus.exceptions.ReportNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class DelegatingReportService {

    private final List<ReportService> handlers;

    public List<SalesDataDto> prepareReportByName(String reportName) {
        return handlers.stream()
                .filter(h -> h.supports(reportName))
                .findAny()
                .map(ReportService::prepareReport)
                .orElseThrow(() -> new ReportNotFoundException("Handler not found for report " + reportName))
                .stream().limit(10).toList();
    }
}
