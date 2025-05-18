package org.example.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dao.ExchangeRateDao;
import org.example.currencyexchange.dto.ExchangeRateRequestDto;
import org.example.currencyexchange.dto.ExchangeRateResponseDto;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.example.currencyexchange.exeption.InvalidParameterException;
import org.example.currencyexchange.service.ExchangeRateService;
import org.example.currencyexchange.utils.MappingUtils;
import org.example.currencyexchange.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static org.example.currencyexchange.utils.MappingUtils.convertToDto;

@WebServlet(name = "ExchangeRatesServlet", urlPatterns = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        objectMapper.writeValue(resp.getWriter(), exchangeRateService.getExchangeRates());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ExchangeRateRequestDto exchangeRateRequestDto = convertRequestToDto(req);

        ExchangeRate exchangeRate = exchangeRateService.save(exchangeRateRequestDto);

        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), convertToDto(exchangeRate));

    }

    private ExchangeRateRequestDto convertRequestToDto(HttpServletRequest req) {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, ValidationUtils.convertToNumber(rate));

        ValidationUtils.validateExchangeRateRequest(exchangeRateRequestDto);

        return exchangeRateRequestDto;
    }

}
