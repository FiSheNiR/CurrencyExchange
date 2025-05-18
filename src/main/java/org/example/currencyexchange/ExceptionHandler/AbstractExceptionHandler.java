package org.example.currencyexchange.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.dto.ErrorResponseDto;

import java.io.IOException;

public abstract class AbstractExceptionHandler<T extends Exception> implements ExceptionHandler {

    private final Class<T> exceptionType;
    private final int httpStatus;
    private final ObjectMapper objectMapper;
    private ExceptionHandler next;

    public AbstractExceptionHandler(
            Class<T> exceptionType,
            int httpStatus,
            ObjectMapper objectMapper) {
        this.exceptionType = exceptionType;
        this.httpStatus = httpStatus;
        this.objectMapper = objectMapper;
    }

    @Override
    public void setNext(ExceptionHandler next) {
        this.next = next;
    }

    @Override
    public void handle(Exception ex, HttpServletResponse response) throws IOException {
        if (exceptionType.isInstance(ex)) {
            response.setStatus(httpStatus);
            ErrorResponseDto dto = new ErrorResponseDto(httpStatus, ex.getMessage());
            objectMapper.writeValue(response.getWriter(), dto);
        } else if (next != null) {
            next.handle(ex, response);
        }
    }
}
