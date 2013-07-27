package ru.nsk.test.cabinet.core;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.nsk.test.cabinet.core.response.CommonResponseFactory;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global application exception handler. Class wrap errors to common
 * envelope before returning error to client side.
 * 
 * @author me
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleSqlError(RuntimeException ex, WebRequest request) {
        return CommonResponseFactory.getCommonResponse(ex);
    }
}