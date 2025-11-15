package ru.otus.services;

import lombok.RequiredArgsConstructor;
import ru.otus.dto.SalesDataDto;

import java.util.List;

import static ru.otus.utils.ReportsUtils.sortItems;

@RequiredArgsConstructor
public class SextantReportService implements ReportService {

    private final CommercialService commercialService;

    public List<SalesDataDto> prepareReport() {
        var reportItems = commercialService.getSextantSalesData();
        return sortItems(reportItems);
    }

    @Override
    public boolean supports(String reportName) {
        return "SextantReport".equalsIgnoreCase(reportName);
    }
}
