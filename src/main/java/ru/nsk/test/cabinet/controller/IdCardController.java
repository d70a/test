package ru.nsk.test.cabinet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.nsk.test.cabinet.pojo.IdCard;
import ru.nsk.test.cabinet.pojo.User;
import ru.nsk.test.cabinet.repository.IdCardRepository;

/**
 * Controller implements standard GET and POST request for IdCard repository.
 *
 * @author me
 */
@Slf4j
@Controller
public class IdCardController extends RestControllerImpl {

    @Autowired
    IdCardRepository idCardRepository;

    /**
     * Simply implementation of GET request. Method load user object from
     * database (with respect to session user), load card id object from user
     * object and return common response based on card id.
     *
     * @return Common response object from response factory.
     */
    @RequestMapping(value = Mappings.IDCARD, method = RequestMethod.GET)
    public ResponseEntity<CommonResponse> get() {
        log.debug("Process GET request");

        final User sessionUser = userRepository.getUserFromSession();

        return getResponse(sessionUser.getIdCard());
    }

    /**
     * Method process POST request for id card object. Before id card object
     * persistence we check database for duplicates of serial and name fields.
     * If id card with the same serial and number found and this is not our user
     * record, then code throws (@see AlreadyRegisteredException). If duplicate 
     * not found we simply store id card for current session user.
     * 
     * In highly concurrent environment this check not efficient because
     * performed not at database level. Therefore database will be in
     * consistent state because (@see IdCard) entity had unique constraint
     * for serial and number fields. In concurrent environment second (last)
     * client with same id card gets HTTP 500 error instead of common response
     * object.
     *
     * @param remote Id card object from client
     *
     * @return Common response object from response factory.
     */
    @RequestMapping(value = Mappings.IDCARD, method = RequestMethod.POST)
    public ResponseEntity<CommonResponse> post(
            @RequestBody IdCard remote) {
        log.debug("Process POST request for id card {}", remote);

        if (remote != null) {
            User sessionUser = userRepository.getUserFromSession();

            /**
             * Validate client id card for unique values
             */
            IdCard check = idCardRepository.findBySerialAndNumber(
                    remote.getSerial(), remote.getNumber());
            if (check != null && !sessionUser.equals(check.getUser())) {
                log.warn("Client {} try to register id card from "
                        + "existing database account {}",
                        sessionUser, check);
                throw new AlreadyRegisteredException(
                        new CommonResponseCodes[]{
                    CommonResponseCodesImpl.ALREADY_REGISTERED_IDCARD});
            }

            /**
             * Success, id card unique
             */
            IdCard local = sessionUser.getIdCard();
            if (local == null) {
                local = new IdCard();
            }

            local.setNumber(remote.getNumber());
            local.setSerial(remote.getSerial());
            local.setBirthDate(remote.getBirthDate());

            idCardRepository.save(local);
        }

        return getResponse();
    }
}
