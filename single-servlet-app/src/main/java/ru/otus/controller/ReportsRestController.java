package ru.otus.controller;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.annotation.AnotherOneRestController;
import ru.otus.annotation.HttpHandler;
import ru.otus.services.DelegatingReportService;

import java.io.IOException;

@RequiredArgsConstructor
@AnotherOneRestController
public class ReportsRestController {

    private final Gson gson;
    private final DelegatingReportService reportService;

    @HttpHandler(mapping = "/api/reports")
    protected void getReport(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var reportName = req.getParameter("reportName");
        var report = reportService.prepareReportByName(reportName);
        var json = gson.toJson(report);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}