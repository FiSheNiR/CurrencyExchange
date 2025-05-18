package org.example.currencyexchange.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ExceptionHandler {
    void handle(Exception ex, HttpServletResponse response) throws IOException;
    void setNext(ExceptionHandler next);
}
