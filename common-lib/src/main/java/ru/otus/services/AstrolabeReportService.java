package ru.otus.services;

import lombok.RequiredArgsConstructor;
import ru.otus.dto.SalesDataDto;

import java.util.List;

import static ru.otus.utils.ReportsUtils.sortItems;

@RequiredArgsConstructor
public class AstrolabeReportService implements ReportService {

    private final CommercialService commercialService;

    @Override
    public List<SalesDataDto> prepareReport() {
        var reportItems = commercialService.getAstrolabeSalesData();
        return sortItems(reportItems);
    }

    @Override
    public boolean supports(String reportName) {
        return "AstrolabeReport".equalsIgnoreCase(reportName);
    }
}
