package org.example.currencyexchange.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dto.ExchangeRequestDto;
import org.example.currencyexchange.dto.ExchangeResponseDto;
import org.example.currencyexchange.service.ExchangeService;
import org.example.currencyexchange.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "ExchangeServlet", urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = new ExchangeService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ExchangeRequestDto exchangeRequestDto = convertToExchangeRequestDto(req);

        ExchangeResponseDto exchangeResponseDto = exchangeService.exchange(exchangeRequestDto);

        objectMapper.writeValue(resp.getWriter(), exchangeResponseDto);
    }

    private ExchangeRequestDto convertToExchangeRequestDto(HttpServletRequest req) {

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        BigDecimal currentAmount = ValidationUtils.convertToNumber(amount);

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, currentAmount);

        ValidationUtils.validateExchangeRequest(exchangeRequestDto);

        return exchangeRequestDto;
    }
}
