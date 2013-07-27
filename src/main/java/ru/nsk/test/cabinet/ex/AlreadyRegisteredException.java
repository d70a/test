package ru.nsk.test.cabinet.ex;

import ru.nsk.test.cabinet.core.response.CommonResponseCodes;

/**
 * Must be thrown if server side detected attempt to duplicate some database
 * records like e-mail, phone or id card number. 
 * 
 * @author me
 */
public class AlreadyRegisteredException extends RuntimeException implements ValidatorError {

    CommonResponseCodes[] codes;
    public AlreadyRegisteredException(CommonResponseCodes [] errors) {
        super();
        this.codes = errors;
    }

    @Override
    public CommonResponseCodes[] getErrorCodes() {
        return codes;
    }
}
