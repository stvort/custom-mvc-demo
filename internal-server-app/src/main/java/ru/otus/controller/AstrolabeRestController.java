package ru.otus.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.dto.BuyRequestDto;
import ru.otus.services.CommercialService;

import java.io.IOException;

@RequiredArgsConstructor
public class AstrolabeRestController extends HttpServlet {
    private final Gson gson;
    private final CommercialService commercialService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var buyRequestDto = gson.fromJson(req.getReader(), BuyRequestDto.class);
        commercialService.sellAstrolabe(buyRequestDto.getCount());
        resp.setStatus(201);
    }
}