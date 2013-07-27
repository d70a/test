package ru.nsk.test.cabinet.utils;

import java.util.Set;
import javax.persistence.Entity;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nsk.test.cabinet.Mappings;
import ru.nsk.test.cabinet.core.response.CommonResponse;
import ru.nsk.test.cabinet.core.response.CommonResponseFactory;
import ru.nsk.test.cabinet.pojo.User;

/**
 * User validator implementation. Used for server side validation of complex
 * user objects with associations. Needed for service UI step-by-step
 * registration form. Class check user (and related) objects in database and
 * recommends to UI some steps (aka actions).
 * 
 * @author me
 */
@Slf4j
public class UserValidatorImpl<T> implements UserValidator<T> {

    private User user;
    private Validator validator;

    public UserValidatorImpl(User user) {
        this.user = user;

        init();
    }

    /**
     * Method check user (and related) objects and recommends UI actions for
     * fill-up user object to 'validates' state.
     * 
     * @return Recommended action string.
     */
    @Override
    public String getRecommendedAction() {
        log.debug("Validate user {} for recommended action", user);

        return validate(new TestEntity(user, Mappings.A_NEEDED_REG_STEP_1),
                new TestEntity(user.getIdCard(), Mappings.A_NEEDED_REG_STEP_2),
                new TestEntity(user.getPlace(), Mappings.A_NEEDED_REG_STEP_3));
    }

    /**
     * Method return common response object with recommended actions.
     * 
     * @return Common response object.
     */
    @Override
    public ResponseEntity<CommonResponse> getResponse() {
        return getResponse((T) user);
    }

    /**
     * Method return common response object with recommended actions.
     * 
     * @param entity Server side entity for client side.
     * @return Common response object.
     */
    @Override
    public ResponseEntity<CommonResponse> getResponse(T entity) {
        String action = getRecommendedAction();

        if (entity == null) {
            return CommonResponseFactory.getCommonResponse(Mappings.EMPTY,
                    HttpStatus.OK, action);
        } else {
            return CommonResponseFactory.getCommonResponse(entity,
                    HttpStatus.OK, action);

        }
    }

    /**
     * Method perform entity validation. Supported any entity with JPA 
     * validation annotations. Validation performed by runtime accessible 
     * validator (usually this is hibernate validator).
     * 
     * @param args (@see TestEntity) object with entity and recommended action.
     * @return 
     */
    private String validate(TestEntity<Entity>... args) {
        for (TestEntity<Entity> e : args) {
            if (e.getEntity() == null) {
                return e.getAction();
            }
            Set<ConstraintViolation<Entity>> errors = validator.validate(e.getEntity());
            if (!errors.isEmpty()) {
                logInvalidated(errors, e.getAction());

                return buildAction(e.getAction());
            }
        }
        // if entity valid, return valid status
        return Mappings.A_VALID;
    }

    private void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private String buildAction(String action) {
        logAction(action);
        return action;
    }

    private void logInvalidated(Set<ConstraintViolation<Entity>> errors, String action) {
        if (log.isDebugEnabled()) {
            for (ConstraintViolation cv : errors) {
                log.debug(
                        "Validate failed for field: {}, value: {}, message: {}",
                        cv.getPropertyPath(), cv.getInvalidValue(),
                        cv.getMessage());
            }
            logAction(
                    action);
        }
    }

    private void logAction(String action) {
        log.debug("Recommended action is: {}",
                action);
    }

    @Data
    private class TestEntity<T> {

        private T entity;
        private String action;

        public TestEntity(T entity, String action) {
            this.entity = entity;
            this.action = action;
        }
    }
}
