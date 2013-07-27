package ru.nsk.test.cabinet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nsk.test.cabinet.Mappings;
import ru.nsk.test.cabinet.core.response.CommonResponse;
import ru.nsk.test.cabinet.ex.LoginInvalidAttemptException;
import ru.nsk.test.cabinet.pojo.User;

/**
 * Controller implements register request (
 *
 * @see register) and perform standard handling for not authenticated requests (
 * @see login).
 *
 */
@Slf4j
@Controller
@RequestMapping(Mappings.AUTH)
public class AuthController extends RestControllerImpl {

    /**
     * All not authenticated users moved to this endpoint by spring security
     * filter. At this place we simply throw (
     *
     * @see LoginInvalidAttemptException) for our exception processing filter.
     * Subsequent error handling out of scope of this controller.
     *
     * @return nothing
     */
    @RequestMapping(value = Mappings.LOGIN, method = RequestMethod.GET)
    public void login() {
        log.debug("Process GET request");
        throw new LoginInvalidAttemptException();

    }

    /**
     * Method register new client at server side.
     *
     * @param email Unique email address, used as login.
     * @param password Any password string
     * @return Common response object from response factory.
     */
    @RequestMapping(value = Mappings.REGISTER, method = RequestMethod.POST)
    public ResponseEntity<CommonResponse> register(
            @RequestParam(value = Mappings.AUTH_FIELD_LOGIN) String email,
            @RequestParam(value = Mappings.AUTH_FIELD_PASSWORD) String password) {

        log.debug(
                "Registration request for email {}, password length {}",
                email, (password == null ? null : password.length()));

        User user = userRepository.register(email, password);

        return getResponse(user);

    }
}