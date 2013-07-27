package ru.nsk.test.cabinet.core.response;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nsk.test.cabinet.Mappings;
import ru.nsk.test.cabinet.ex.ValidatorError;

/**
 * Factory create common response object for client side. Current implementation
 * load error mapping from (@see CommonResponseCodes).
 *
 * @author me
 */
@Slf4j
public class CommonResponseFactory {

    private final static HashMap<Class, CommonResponseCodes> container;

    /**
     * Load error mapping (exception -> to -> CommonResponseCodes) from 
     * CommonResponseCodesImpl class.
     */
    static {
        container = new HashMap();

        for (CommonResponseCodes c : CommonResponseCodesImpl.values()) {
            Class[] exceptions = c.exception();
            if (exceptions == null) {
                log.warn("Handler {} have no exceptions.", c);
                continue;
            }
            for (Class exception : exceptions) {
                log.trace("Create handler {} for exception {}",
                        c, exception);
                container.put(exception, c);
            }
        }
    }

    /**
     * Method build common error response based on exception type.
     *
     * @param t Throwable instance, trapped by application.
     * @return Response entity, ready for sending to client.
     */
    public static ResponseEntity getCommonResponse(Throwable t) {
        log.trace("Create response entity for: {}", t);


        CommonResponseCodes crc = container.get(t.getClass());
        log.debug("Found handler {} for exception {}", crc, t);
        if (crc == null) {
            // assign default unknown error
            crc = CommonResponseCodesImpl.UNKNOW_ERROR;
            log.debug("Assign default handler {} for exception {}", crc, t);
        }

        CommonResponse cr = new CommonResponseImpl(
                crc.code(),
                crc.description(), Mappings.EMPTY,
                crc.action(),
                t instanceof ValidatorError
                ? ((ValidatorError) t).getErrorCodes() : new CommonResponseCodes[]{});
        ResponseEntity re = new ResponseEntity(cr, crc.status());

        log.trace("Return response entity: {}", re);

        return re;
    }

    /**
     * Method build success response object.
     *
     * @param <T> Real payload type.
     * @param p Real payload object returned by our application.
     * @param httpStatus Application offered HTTP status code.
     * @return Response entity, ready for sending to client.
     */
    public static <T> ResponseEntity getCommonResponse(T p, HttpStatus httpStatus) {
        return getCommonResponse(p, httpStatus, Mappings.EMPTY);
    }

    public static <T> ResponseEntity getCommonResponse(T p, HttpStatus httpStatus, String action) {
        log.trace("Create response entity for status: {}, object {}",
                httpStatus, p);

        String status = CommonResponseCodesImpl.SUCCESS.code();
        String description = CommonResponseCodesImpl.SUCCESS.description();

        CommonResponse cr = new CommonResponseImpl(
                status,
                description, p, action, null);
        ResponseEntity re = new ResponseEntity(cr, httpStatus);

        log.trace("Return response entity: {}", re);

        return re;
    }
}
