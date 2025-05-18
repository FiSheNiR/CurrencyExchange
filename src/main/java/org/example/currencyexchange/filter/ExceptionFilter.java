package org.example.currencyexchange.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.ExceptionHandler.*;
import org.example.currencyexchange.dto.ErrorResponseDto;
import org.example.currencyexchange.exeption.DatabaseOperationException;
import org.example.currencyexchange.exeption.EntityExistsException;
import org.example.currencyexchange.exeption.InvalidParameterException;
import org.example.currencyexchange.exeption.NotFoundException;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebFilter("/*")
public class ExceptionFilter extends HttpFilter {
    private final ExceptionHandler exceptionHandlerChain;
    static ObjectMapper objectMapper = new ObjectMapper();

    public ExceptionFilter() {
        exceptionHandlerChain = createDefaultExceptionHandlerChain(objectMapper);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            super.doFilter(req, res, chain);
        } catch (Exception e) {
            exceptionHandlerChain.handle(e, res);
        }
    }

    private static ExceptionHandler createDefaultExceptionHandlerChain(ObjectMapper mapper) {
        ExceptionHandler chain = new DatabaseOperationHandler(objectMapper);
        chain.setNext(new EntityExistsHandler(objectMapper));
        chain.setNext(new InvalidParameterHandler(objectMapper));
        chain.setNext(new NotFoundHandler(objectMapper));
        chain.setNext(new DefaultExceptionHandler(objectMapper));
        return chain;
    }
}
