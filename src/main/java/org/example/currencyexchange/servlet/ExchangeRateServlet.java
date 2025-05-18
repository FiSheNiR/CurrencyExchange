package org.example.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dto.ExchangeRateRequestDto;
import org.example.currencyexchange.enitity.ExchangeRate;
import org.example.currencyexchange.exeption.InvalidParameterException;
import org.example.currencyexchange.service.ExchangeRateService;
import org.example.currencyexchange.utils.MappingUtils;
import org.example.currencyexchange.utils.ValidationUtils;

import java.io.IOException;

@WebServlet(name = "ExchangeRateServlet" , urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String code = req.getPathInfo().replaceAll("/","");

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCode(code);

        objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDto(exchangeRate));

    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String code = req.getPathInfo().replaceAll("/","");
        String rate = req.getReader().readLine();

        ValidationUtils.validateExchangeRate(code);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(code.substring(0,3), code.substring(3,6), ValidationUtils.validateRate(rate));
        ExchangeRate exchangeRate = exchangeRateService.update(exchangeRateRequestDto);

        objectMapper.writeValue(resp.getWriter(), MappingUtils.convertToDto(exchangeRate));

    }
}
