/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.nsk.test.cabinet.utils;

import org.springframework.http.ResponseEntity;
import ru.nsk.test.cabinet.core.response.CommonResponse;

/**
 * User validation interface, can be used for build validator factory implementation.
 * @author me
 */
public interface UserValidator<T> {

    public String getRecommendedAction();
    
    public ResponseEntity<CommonResponse> getResponse();
    
    public ResponseEntity<CommonResponse> getResponse(T entity);
}
