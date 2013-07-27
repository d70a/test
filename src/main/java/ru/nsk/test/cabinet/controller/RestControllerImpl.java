package ru.nsk.test.cabinet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ru.nsk.test.cabinet.core.response.CommonResponse;
import ru.nsk.test.cabinet.pojo.User;
import ru.nsk.test.cabinet.repository.UserRepository;
import ru.nsk.test.cabinet.utils.UserValidator;
import ru.nsk.test.cabinet.utils.UserValidatorImpl;

/**
 * Common response builder implementation.
 * 
 * @author me
 */
public class RestControllerImpl<T> implements RestController<T> {

    @Autowired
    UserRepository userRepository;

    /**
     * Method return common response for requests which not returns any
     * entities.
     * 
     * @return Common response object from response factory.
     */
    @Override
    public ResponseEntity<CommonResponse> getResponse() {
        return getResponse(null);
    }

    /**
     * Method return common response for requests which returns some entities.
     * Current implementation create internal entity validator and build
     * validator driven response.
     * 
     * @param entity
     * @return Common response object from response factory..
     */
    @Override
    public ResponseEntity<CommonResponse> getResponse(T entity) {
        User sessionUser = userRepository.getUserFromSession();

        UserValidator validator = new UserValidatorImpl(sessionUser);

        if (entity == null) {
            return validator.getResponse();
        } else {
            return validator.getResponse(entity);
        }
    }
}
