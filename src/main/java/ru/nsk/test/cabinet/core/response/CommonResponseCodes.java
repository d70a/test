package ru.nsk.test.cabinet.core.response;

import org.springframework.http.HttpStatus;

/**
 * Common application response codes interface.
 *
 * @author me
 */
public interface CommonResponseCodes {

    /**
     * 
     * @return Response HTTP status code.
     */
    public HttpStatus status();
    
    /**
     * @return Status code string representation.
     */
    public String code();

    /**
     *
     * @return Description of error
     */
    public String description();

    /**
     *
     * @return list of supported exception
     */
    public Class[] exception();
    
    /**
     * 
     * @return Action, recommended for client by server side.
     */
    public String action();
}
