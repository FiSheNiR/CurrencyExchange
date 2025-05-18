package org.example.currencyexchange.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.currencyexchange.exeption.DefaultException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class DefaultExceptionHandler extends AbstractExceptionHandler<DefaultException>{
    public DefaultExceptionHandler(ObjectMapper objectMapper) {
        super(DefaultException.class, SC_INTERNAL_SERVER_ERROR, objectMapper);
    }
}
