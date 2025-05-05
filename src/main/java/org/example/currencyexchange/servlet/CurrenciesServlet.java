package org.example.currencyexchange.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dao.CurrencyDao;
import org.example.currencyexchange.dto.CurrencyRequestDto;
import org.example.currencyexchange.dto.CurrencyResponseDto;
import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.utils.MappingUtils;
import org.example.currencyexchange.utils.ValidationUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

@WebServlet(name = "CurrenciesServlet", urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Currency> currencies = currencyDao.findAllCurrency();

        List<CurrencyResponseDto> currenciesDto = currencies.stream()
                .map(MappingUtils::convertToDto)
                .collect(Collectors.toList());

        objectMapper.writeValue(response.getWriter(), currenciesDto);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);

        ValidationUtils.validateCurrencyRequest(currencyRequestDto);

        Currency currency = currencyDao.saveCurrency(MappingUtils.convertDtoToEntity(currencyRequestDto));

        response.setStatus(SC_CREATED);
        objectMapper.writeValue(response.getWriter(), MappingUtils.convertToDto(currency));
    }
}
