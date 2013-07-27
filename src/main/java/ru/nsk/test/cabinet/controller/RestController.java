package ru.nsk.test.cabinet.controller;

import org.springframework.http.ResponseEntity;
import ru.nsk.test.cabinet.core.response.CommonResponse;

/**
 * Common response interface for simple REST controllers.
 * 
 * @author me
 */
public interface RestController<T> {

    public ResponseEntity<CommonResponse> getResponse();
    public ResponseEntity<CommonResponse> getResponse(T e);
}
