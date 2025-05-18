package org.example.currencyexchange.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.currencyexchange.exeption.InvalidParameterException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class InvalidParameterHandler extends AbstractExceptionHandler<InvalidParameterException> {
    public InvalidParameterHandler(ObjectMapper objectMapper) {
        super(InvalidParameterException.class, SC_BAD_REQUEST, objectMapper);
    }
}
