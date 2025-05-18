package org.example.currencyexchange.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.exeption.DatabaseOperationException;

public class DatabaseOperationHandler extends AbstractExceptionHandler<DatabaseOperationException> {
    public DatabaseOperationHandler(ObjectMapper mapper) {
        super(DatabaseOperationException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, mapper);
    }
}