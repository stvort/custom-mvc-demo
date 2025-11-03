package ru.otus;

import com.google.gson.GsonBuilder;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import ru.otus.controller.AstrolabeRestController;
import ru.otus.controller.ReportsRestController;
import ru.otus.controller.SextantRestController;
import ru.otus.services.AstrolabeReportService;
import ru.otus.services.CommercialService;
import ru.otus.services.DelegatingReportService;
import ru.otus.services.SextantReportService;

import java.io.File;
import java.util.List;

public class Application {
    public static void main(String[] args) throws LifecycleException {
        var tomcat = new Tomcat();
        tomcat.setBaseDir("internal-server-app/temp");
        tomcat.setPort(8080);

        var contextPath = "/app";

        var docBase = new File("internal-server-app/src/main/webapp").getAbsolutePath();
        var context = tomcat.addContext(contextPath, docBase);

        context.addWelcomeFile("index.html");
        var resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/",
                docBase, "/"));

        context.setResources(resources);

        var commercialService = new CommercialService();

        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        var astrolabeReportService = new AstrolabeReportService(commercialService);
        var sextantReportService = new SextantReportService(commercialService);
        var reportService = new DelegatingReportService(List.of(astrolabeReportService,
                sextantReportService));

        var reportsRestController = new ReportsRestController(gson, reportService);
        var astrolabeRestController = new AstrolabeRestController(gson, commercialService);
        var sextantRestController = new SextantRestController(gson, commercialService);

        var defaultControllerWrapper = Tomcat.addServlet(context, "default", new DefaultServlet());
        var reportsRestControllerWrapper = tomcat.addServlet(contextPath, "ReportsRestController",
                reportsRestController);
        var astrolabeRestControllerWrapper = tomcat.addServlet(contextPath, "AstrolabeRestController",
                astrolabeRestController);
        var sextantRestControllerWrapper = tomcat.addServlet(contextPath, "SextantRestController",
                sextantRestController);

        context.addServletMappingDecoded("/", defaultControllerWrapper.getName());
        context.addServletMappingDecoded("/api/reports", reportsRestControllerWrapper.getName());
        context.addServletMappingDecoded("/api/astrolabe", astrolabeRestControllerWrapper.getName());
        context.addServletMappingDecoded("/api/sextant", sextantRestControllerWrapper.getName());

        tomcat.start();
        tomcat.getConnector();
        System.out.println("http://localhost:8080/app");
        tomcat.getServer().await();
    }
}
