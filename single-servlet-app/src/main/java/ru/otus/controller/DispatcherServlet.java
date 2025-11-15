package ru.otus.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.annotation.AnotherOneRestController;
import ru.otus.annotation.HttpHandler;
import ru.otus.annotation.HttpMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

public class DispatcherServlet extends HttpServlet {

    private final Map<HandlerMapping, ControllerMethod> handlersMapping;

    public DispatcherServlet(List<Object> controllers) {
        this.handlersMapping = new ConcurrentHashMap<>();

        for (Object controller : controllers) {
            var controllerClass = controller.getClass();
            if (!controllerClass.isAnnotationPresent(AnotherOneRestController.class)) {
                continue;
            }
            for (Method controllerMethod : controllerClass.getDeclaredMethods()) {
                var httpHandlerAnnotation = controllerMethod.getAnnotation(HttpHandler.class);
                if (isNull(httpHandlerAnnotation)) {
                    continue;
                }
                handlersMapping.put(
                        HandlerMapping.of(httpHandlerAnnotation),
                        ControllerMethod.of(controller, controllerMethod)
                );
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var path = Optional.ofNullable(req.getServletPath()).orElse("/index.html");
        var mapping = handlersMapping.get(HandlerMapping.of(req.getMethod(), path));
        try {
            if (isNull(mapping)) {
                processStaticResource(path, req, resp);
                return;
            }
            mapping.invoke(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void processStaticResource(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (var staticResource = req.getServletContext().getResourceAsStream(path)) {
            if (isNull(staticResource)) {
                resp.setStatus(404);
                return;
            }
            staticResource.transferTo(resp.getOutputStream());
        }
    }

    private record HandlerMapping(HttpMethod httpMethod, String mapping) {
        public static HandlerMapping of(HttpHandler httpHandler) {
            return new HandlerMapping(httpHandler.method(), httpHandler.mapping());
        }

        public static HandlerMapping of(String httpMethod, String mapping) {
            return new HandlerMapping(HttpMethod.valueOf(httpMethod.toUpperCase(Locale.ROOT)), mapping);
        }
    }

    private record ControllerMethod(Object controller, Method method) {
        public static ControllerMethod of(Object controller, Method method) {
            return new ControllerMethod(controller, method);
        }

        public void invoke(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            method.invoke(controller, req, resp);
        }
    }
}