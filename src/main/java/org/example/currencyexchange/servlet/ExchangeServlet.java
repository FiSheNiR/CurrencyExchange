package org.example.currencyexchange.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dto.ExchangeRequestDto;
import org.example.currencyexchange.dto.ExchangeResponseDto;
import org.example.currencyexchange.service.ExchangeService;
import org.example.currencyexchange.utils.ValidationUtils;

import java.io.IOException;

@WebServlet(name = "ExchangeServlet", urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = new ExchangeService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, ValidationUtils.convertToNumber(amount));

        ValidationUtils.validateExchangeRequest(exchangeRequestDto);

        ExchangeResponseDto exchangeResponseDto = exchangeService.exchange(exchangeRequestDto);

        objectMapper.writeValue(resp.getWriter(), exchangeResponseDto);
    }
}
