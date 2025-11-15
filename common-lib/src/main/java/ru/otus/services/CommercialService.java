package ru.otus.services;

import ru.otus.dto.SalesDataDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.otus.utils.ReportsUtils.generateReportItems;

public class CommercialService {
    private static final String ASTROLABE_ITEM = "Астролябия";
    private static final String SEXTANT_ITEM = "Секстант";

    private final Map<Integer, SalesDataDto> astrolabeSalesByYear;
    private final Map<Integer, SalesDataDto> sextantSalesByYear;

    public CommercialService() {
        astrolabeSalesByYear = generateReportItems(ASTROLABE_ITEM, 50).stream()
                .collect(Collectors.toMap(SalesDataDto::getYear, Function.identity()));
        sextantSalesByYear = generateReportItems(SEXTANT_ITEM, 50).stream()
                .collect(Collectors.toMap(SalesDataDto::getYear, Function.identity()));
    }

    public void sellAstrolabe(int count) {
        sellItem(astrolabeSalesByYear, ASTROLABE_ITEM, count);
    }

    public void sellSextant(int count) {
        sellItem(sextantSalesByYear, SEXTANT_ITEM, count);
    }

    public List<SalesDataDto> getAstrolabeSalesData() {
        return new ArrayList<>(astrolabeSalesByYear.values());
    }

    public List<SalesDataDto> getSextantSalesData() {
        return new ArrayList<>(sextantSalesByYear.values());
    }

    private void sellItem(Map<Integer, SalesDataDto> storage, String itemName, int count) {
        var dto = storage.computeIfAbsent(LocalDate.now().getYear(),
                k -> new SalesDataDto(k, itemName, 0));
        dto.setAmount(dto.getAmount() + count);
    }
}
