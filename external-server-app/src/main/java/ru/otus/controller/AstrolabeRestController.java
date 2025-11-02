package ru.otus.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.dto.BuyRequestDto;
import ru.otus.services.CommercialService;

import java.io.IOException;

public class AstrolabeRestController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var gson = (Gson) req.getServletContext().getAttribute("gson");
        CommercialService commercialService = (CommercialService) req.getServletContext()
                .getAttribute("commercialService");

        var buyRequestDto = gson.fromJson(req.getReader(), BuyRequestDto.class);
        commercialService.sellAstrolabe(buyRequestDto.getCount());
        resp.setStatus(201);
    }
}