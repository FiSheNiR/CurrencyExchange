package org.example.currencyexchange.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.enitities.Currency;
import org.example.currencyexchange.enitities.ErrorResponse;
import org.example.currencyexchange.services.CurrencyService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CurrenciesServlet", urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Currency> currencies = null;
        try {
            currencies = currencyService.findAllCurrencies();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(),new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
        objectMapper.writeValue(response.getWriter(), currencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if (name == null || name.isBlank() || code == null || code.isBlank() || sign == null || sign.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(),new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужное поле формы"));
            return;
        }

        try {
            Currency currency = new Currency(code, name, sign);
            Integer savedCurrencyId = currencyService.addCurrency(currency);
            currency.setId(savedCurrencyId);
            objectMapper.writeValue(response.getWriter(), currency);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(),new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
