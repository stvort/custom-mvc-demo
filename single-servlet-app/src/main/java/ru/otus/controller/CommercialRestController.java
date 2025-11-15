package ru.otus.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.annotation.AnotherOneRestController;
import ru.otus.annotation.HttpHandler;
import ru.otus.dto.BuyRequestDto;
import ru.otus.services.CommercialService;

import java.io.IOException;

import static ru.otus.annotation.HttpMethod.POST;

@RequiredArgsConstructor
@AnotherOneRestController
public class CommercialRestController{
    private final Gson gson;
    private final CommercialService commercialService;

    @HttpHandler(method = POST, mapping = "/api/astrolabe")
    public void buyAstrolabe(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var buyRequestDto = gson.fromJson(req.getReader(), BuyRequestDto.class);
        commercialService.sellAstrolabe(buyRequestDto.getCount());
        resp.setStatus(201);
    }

    @HttpHandler(method = POST, mapping = "/api/sextant")
    public void buySextant(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var buyRequestDto = gson.fromJson(req.getReader(), BuyRequestDto.class);
        commercialService.sellSextant(buyRequestDto.getCount());
        resp.setStatus(201);
    }
}