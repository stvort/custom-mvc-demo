package ru.otus.utils;

import ru.otus.dto.SalesDataDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ReportsUtils {

    public static List<SalesDataDto> generateReportItems(String product, int linesCount) {
        var random = new Random();
        var items = new ArrayList<SalesDataDto>();
        for (int i = 1; i <= linesCount; i++) {
            int year = LocalDate.now().getYear() - linesCount + i;
            var amount = random.nextInt(100);
            items.add(new SalesDataDto(year, product, amount));
        }
        return items;
    }

    public static List<SalesDataDto> sortItems(List<SalesDataDto> reportItems) {
        reportItems.sort(Comparator.comparingInt(SalesDataDto::getYear).reversed()
                .thenComparingInt(SalesDataDto::getAmount));
        return reportItems;
    }
}
