package ru.nsk.test.cabinet.ex;

import ru.nsk.test.cabinet.core.response.CommonResponseCodes;

/**
 *
 * @author me
 */
public interface ValidatorError {
    public CommonResponseCodes[] getErrorCodes();
}
