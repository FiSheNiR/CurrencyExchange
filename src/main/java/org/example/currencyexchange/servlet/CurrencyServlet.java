package org.example.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dao.CurrencyDao;
import org.example.currencyexchange.enitity.Currency;
import org.example.currencyexchange.exeption.NotFoundException;
import org.example.currencyexchange.utils.ValidationUtils;

import java.io.IOException;

@WebServlet(name = "CurrencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getPathInfo().replaceAll("/","");

        ValidationUtils.validateCurrencyCode(code);

        Currency currency = currencyDao.findCurrencyByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + code + "' not found"));

        objectMapper.writeValue(response.getWriter(), currency);
    }
}
