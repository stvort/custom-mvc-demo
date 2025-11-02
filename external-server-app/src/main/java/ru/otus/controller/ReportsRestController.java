package ru.otus.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.services.DelegatingReportService;

import java.io.IOException;

public class ReportsRestController  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var gson = (Gson) req.getServletContext().getAttribute("gson");
        var reportService = (DelegatingReportService) req.getServletContext()
                .getAttribute("reportService");

        var reportName = req.getParameter("reportName");
        var report = reportService.prepareReportByName(reportName);
        String json = gson.toJson(report);
        resp.getWriter().write(json);
    }
}