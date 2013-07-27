package ru.nsk.test.cabinet.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import ru.nsk.test.cabinet.AbstractWebIntegrationTest;
import ru.nsk.test.cabinet.Mappings;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.nsk.test.cabinet.AbstractWebIntegrationTest.SUCCESS;

/**
 *
 * @author me
 */
@Slf4j
public class PlaceControllerTest extends AbstractWebIntegrationTest{
    
    private final static String LOGIN = "place@test.com";
    private final static String PASSWORD = "test";
    
    @Test
    public void getTest() throws Exception {
        /**
         * Login to system (create session)
         */
        register(LOGIN, PASSWORD, status().isOk(), SUCCESS);
        login(LOGIN, PASSWORD);
        
        /**
         * Perform GET request for place.
         */
        
        load(Mappings.PLACE, "", status().isOk(), null, null);

    }

}
