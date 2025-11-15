package ru.otus;

import com.google.gson.GsonBuilder;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import ru.otus.controller.CommercialRestController;
import ru.otus.controller.DispatcherServlet;
import ru.otus.controller.ReportsRestController;
import ru.otus.services.AstrolabeReportService;
import ru.otus.services.CommercialService;
import ru.otus.services.DelegatingReportService;
import ru.otus.services.SextantReportService;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class Application {
    public static void main(String[] args) throws Exception {
        var tomcat = new Tomcat();
        tomcat.setBaseDir("single-servlet-app/temp");
        tomcat.setPort(8080);

        var contextPath = "/app";
        var context = prepareContext(tomcat, contextPath);
        context.addWelcomeFile("index.html");

        var dispatcherServlet = prepareDispatcherServlet();
        var dispatcherServletWrapper = tomcat.addServlet(contextPath, "dispatcherServlet",
                dispatcherServlet);
        context.addServletMappingDecoded("/", dispatcherServletWrapper.getName());

        tomcat.start();
        tomcat.getConnector();
        System.out.println("http://localhost:8080/app");
        tomcat.getServer().await();
    }

    private static DispatcherServlet prepareDispatcherServlet() {
        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        var commercialService = new CommercialService();

        var astrolabeReportService = new AstrolabeReportService(commercialService);
        var sextantReportService = new SextantReportService(commercialService);
        var reportService = new DelegatingReportService(List.of(astrolabeReportService,
                sextantReportService));

        var reportsRestController = new ReportsRestController(gson, reportService);
        var commercialRestController = new CommercialRestController(gson, commercialService);

        return new DispatcherServlet(List.of(reportsRestController, commercialRestController));
    }

    private static Context prepareContext(Tomcat tomcat, String contextPath) throws Exception {
        File jarFile = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String jarPath = jarFile.getAbsolutePath();
        boolean isNotInJar = !jarPath.toLowerCase(Locale.ROOT).endsWith(".jar");
        System.out.println(jarPath);

        if (isNotInJar) {
            var docBase = Paths.get(jarPath, "static").toAbsolutePath().toString();
            return tomcat.addContext(contextPath, docBase);
        }

        var docBase = new File(".").getAbsolutePath();
        var context = tomcat.addContext(contextPath, docBase);
        StandardRoot root = new StandardRoot(context);
        JarResourceSet jarResourceSet = new JarResourceSet(root, "/", jarPath, "/static");
        root.addJarResources(jarResourceSet);
        context.setResources(root);

        return context;
    }
}
