package ru.otus.controller;

import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.otus.services.AstrolabeReportService;
import ru.otus.services.CommercialService;
import ru.otus.services.DelegatingReportService;
import ru.otus.services.SextantReportService;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.util.List;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(servletContext, "/WEB-INF/views");
        CommercialService commercialService = new CommercialService();

        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        var astrolabeReportService = new AstrolabeReportService(commercialService);
        var sextantReportService = new SextantReportService(commercialService);
        DelegatingReportService reportService = new DelegatingReportService(List.of(astrolabeReportService,
                sextantReportService));

        servletContext.setAttribute("gson", gson);
        servletContext.setAttribute("templateProcessor", templateProcessor);
        servletContext.setAttribute("commercialService", commercialService);
        servletContext.setAttribute("reportService", reportService);
    }
}
