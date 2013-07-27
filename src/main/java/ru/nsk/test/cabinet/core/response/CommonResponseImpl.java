package ru.nsk.test.cabinet.core.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import ru.nsk.test.cabinet.Mappings;

/**
 * Common response implementation object.
 *
 * @author me
 */
@ToString
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class CommonResponseImpl<T> implements CommonResponse {

    private final String status;
    private final String description;
    private final T payload;
    private final String action;
    private final String[] errors;
    
    public CommonResponseImpl() {
        this.status = CommonResponseCodesImpl.UNKNOW_ERROR.code();
        this.description = CommonResponseCodesImpl.UNKNOW_ERROR.description();
        this.payload = null;
        this.action = Mappings.EMPTY;
        this.errors = new String[] {};
    }

    public CommonResponseImpl(String status, String description, T payload, String action, CommonResponseCodes[] errors) {
        this.status = status;
        this.description = description;
        this.payload = payload;
        this.action = action;
        this.errors = decodeErrors(errors);
    }

    private String[] decodeErrors(CommonResponseCodes[] codes) {
        if (codes != null) {
            List<String> l = new ArrayList(codes.length);
            for (CommonResponseCodes c : codes) {
                l.add(c.code());
            }
            return l.toArray(new String[]{});
        }
        return null;
    }
}
