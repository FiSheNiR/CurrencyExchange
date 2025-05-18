package org.example.currencyexchange.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchange.exeption.DatabaseOperationException;
import org.example.currencyexchange.exeption.EntityExistsException;

public class EntityExistsHandler extends AbstractExceptionHandler<EntityExistsException>{
    public EntityExistsHandler(ObjectMapper mapper) {
        super(EntityExistsException.class, HttpServletResponse.SC_CONFLICT, mapper);
    }
}
