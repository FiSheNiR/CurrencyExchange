package org.example.currencyexchange.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.currencyexchange.exeption.NotFoundException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class NotFoundHandler extends AbstractExceptionHandler<NotFoundException>{
    public NotFoundHandler(ObjectMapper objectMapper) {
        super(NotFoundException.class, SC_NOT_FOUND, objectMapper);
    }
}
