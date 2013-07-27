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
import ru.nsk.test.cabinet.pojo.Place;
import ru.nsk.test.cabinet.pojo.User;
import ru.nsk.test.cabinet.repository.PlaceRepository;

/**
 *
 * @author me
 */
@Slf4j
@Controller
public class PlaceController extends RestControllerImpl {

    @Autowired
    PlaceRepository placeRepository;

    /**
     * Simply implementation of GET request. Method load user object from
     * database (with respect to session user), load place object from user
     * object and return common response based on place.
     *
     * @return Common response object from response factory.
     */
    @RequestMapping(value = Mappings.PLACE, method = RequestMethod.GET)
    public ResponseEntity<CommonResponse> get() {
        log.debug("Process GET request");

        final User sessionUser = userRepository.getUserFromSession();

        return getResponse(sessionUser.getPlace());
    }

    /**
     * Method process POST request for place object.
     *
     * @param remote object from client
     * 
     * @return Common response object from response factory.
     */
    @RequestMapping(value = Mappings.PLACE, method = RequestMethod.POST)
    public ResponseEntity<CommonResponse> post(
            @RequestBody Place remote) {
        log.debug("Process POST request for place {}", remote);

        if (remote != null) {
            User sessionUser = userRepository.getUserFromSession();

            Place local = sessionUser.getPlace();
            if (local == null) {
                local = new Place();
            }

            local.setRegion(remote.getRegion());
            local.setCity(remote.getCity());
            local.setAddress(remote.getAddress());

            placeRepository.save(local);
        }

        return getResponse();
    }
}
