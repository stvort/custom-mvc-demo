package ru.otus.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.services.DelegatingReportService;

import java.io.IOException;

@RequiredArgsConstructor
public class ReportsRestController  extends HttpServlet {

    private final Gson gson;
    private final DelegatingReportService reportService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var reportName = req.getParameter("reportName");
        var report = reportService.prepareReportByName(reportName);
        var json = gson.toJson(report);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}