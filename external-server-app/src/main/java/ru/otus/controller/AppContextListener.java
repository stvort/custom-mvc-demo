package ru.otus.controller;

import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.otus.services.AstrolabeReportService;
import ru.otus.services.CommercialService;
import ru.otus.services.DelegatingReportService;
import ru.otus.services.SextantReportService;

import java.util.List;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var servletContext = sce.getServletContext();

        var commercialService = new CommercialService();

        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        var astrolabeReportService = new AstrolabeReportService(commercialService);
        var sextantReportService = new SextantReportService(commercialService);
        var reportService = new DelegatingReportService(List.of(astrolabeReportService,
                sextantReportService));

        servletContext.setAttribute("gson", gson);
        servletContext.setAttribute("commercialService", commercialService);
        servletContext.setAttribute("reportService", reportService);
    }
}
