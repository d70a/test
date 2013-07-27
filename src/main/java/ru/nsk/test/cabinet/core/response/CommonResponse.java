package ru.nsk.test.cabinet.core.response;

/**
 * Application common response interface.
 * 
 * @author me
 */
public interface CommonResponse<T> {
    
    public String getStatus();
    public String getDescription();
    public T getPayload();
    public String getAction();
    public String [] getErrors();
}
