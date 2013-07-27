package ru.nsk.test.cabinet.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.nsk.test.cabinet.Mappings;
import ru.nsk.test.cabinet.core.response.CommonResponse;
import ru.nsk.test.cabinet.core.response.CommonResponseCodes;
import ru.nsk.test.cabinet.core.response.CommonResponseCodesImpl;
import ru.nsk.test.cabinet.ex.AlreadyRegisteredException;
import ru.nsk.test.cabinet.pojo.User;

/**
 * Controller implements standard GET and POST request for User repository.
 */
@Controller
@Slf4j
public class UserController extends RestControllerImpl {

    /**
     * Method return common response object based on user session object.
     * 
     * @return Common response object from response factory.
     */
    @RequestMapping(value = Mappings.USER, method = RequestMethod.GET)
    public ResponseEntity<CommonResponse> get() {
        log.debug("Process GET request");

        return getResponse(userRepository.getUserFromSession());
    }

    /**
     * Method process POST request for user repository. For every request we
     * load valid user object from database with respect to session. Next we
     * combine incoming user object from client with database object. At this
     * stage we split incoming raw phone field to provider code and phone
     * number. If both fields (code and number) are set, them we check database
     * for unique combination for this field. In database already holds this
     * phone for other user we throws (@see AlreadyRegisteredException) for
     * client. If phone is unique, then combined user object will be stored
     * to database.
     * 
     * * In highly concurrent environment this check not efficient because
     * performed not at database level. Therefore database will be in
     * consistent state because (@see User) entity had unique constraint
     * for code and phone fields. In concurrent environment second (last)
     * client with same id card gets HTTP 500 error instead of common response
     * object.
     * 
     * @param user User object from client
     * @return Common response object from response factory.
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse> post(
            @RequestBody User user) {
        log.debug("Process POST request for user {}", user);

        final User sessionUser = userRepository.getUserFromSession();

        if (user != null) {

            /**
             * Combine database data and client data to one object
             */
            combine(sessionUser, user);

            /**
             * Validate client phone for unique values
             */
            if (sessionUser.getCode() != null && sessionUser.getPhone() != null) {
                User check = userRepository.findByCodeAndPhone(
                        sessionUser.getCode(), sessionUser.getPhone());
                if (check != null && !check.equals(sessionUser)) {
                    log.warn("Client {} try to register phone number from "
                            + "existing database account {}",
                            sessionUser, check);
                    throw new AlreadyRegisteredException(
                            new CommonResponseCodes[]{
                        CommonResponseCodesImpl.ALREADY_REGISTERED_PHONE});
                }
            }

            // save combined unique user to parsistense layer
            userRepository.save(sessionUser);
        }

        return getResponse();
    }

    /**
     * Method combine first, middle and last name fields from source to
     * destination user objects. Also source raw phone field splits to
     * code and number for destination object.
     * 
     * @param dst Destination user object
     * @param src Source user object
     */
    protected void combine(User dst, User src) {
        dst.setNameFirst(src.getNameFirst());
        dst.setNameMiddle(src.getNameMiddle());
        dst.setNameLast(src.getNameLast());

        if (src.getPhone() != null) {
            Pattern p = Pattern.compile("(?<code>\\d{3})?(?<number>\\d{7})$");
            Matcher m = p.matcher(src.getPhone()
                    .replaceAll("[^\\d]", ""));
            if (m.find()) {
                String code = m.group("code");
                if (code != null) {
                    dst.setCode(code);
                }
                String phone = m.group("number");
                if (phone != null) {
                    dst.setPhone(phone);
                }
            }
        }
    }
}