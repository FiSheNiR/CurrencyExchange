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
import java.util.Optional;

@WebServlet(name = "CurrencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getPathInfo().replaceAll("/","");

        if (code.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(),new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Код валюты отсутствует в адресе"));
            return;
        }

        try {

            Optional<Currency> currency = currencyService.findCurrencyByCode(code);
            if (currency.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(response.getWriter(),new ErrorResponse(HttpServletResponse.SC_NOT_FOUND, "Валюта не найдена"));
                return;
            }
            objectMapper.writeValue(response.getWriter(), currency.orElse(null));
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(),new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
