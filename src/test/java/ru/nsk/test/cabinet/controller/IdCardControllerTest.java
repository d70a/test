package ru.nsk.test.cabinet.controller;

import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.nsk.test.cabinet.AbstractWebIntegrationTest;
import ru.nsk.test.cabinet.Mappings;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.nsk.test.cabinet.AbstractWebIntegrationTest.SUCCESS;

/**
 *
 * @author me
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IdCardControllerTest extends AbstractWebIntegrationTest {

    private final static String LOGIN_1 = "1@dev.com";
    private final static String PASSWORD_1 = "test";
    private final static String FILE_USER_1 = "user_1.json";
    private final static String FILE_IDCARD = "idcard.json";
    
    private final static String LOGIN_2 = "2@dev.com";
    private final static String PASSWORD_2 = "test";

    @Test
    public void aLogin() throws Exception {
        /**
         * Login to system (create session)
         */
        register(LOGIN_1, PASSWORD_1, status().isOk(), SUCCESS);
        login(LOGIN_1, PASSWORD_1);
        
        register(LOGIN_2, PASSWORD_2, status().isOk(), SUCCESS);

    }

//    @Test
    private void updateIdCardInCurrentSession(ResultMatcher expectedCode, String expectedStatus) throws Exception {

        String endpoint = Mappings.IDCARD;

        byte[] data = loadResource(FILE_IDCARD);

        ResultActions result = mvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .session(sessionHolder.getSession())
                .content(data))
                .andExpect(expectedCode) //
                .andExpect(content().contentType(
                "application/json;charset=UTF-8"));

        String response = result.andReturn().getResponse().getContentAsString();

        log.info(MessageFormat.format("Content of {0} -> {1}",
                endpoint,
                response));

        validateJsonResponse(response, expectedStatus, null, null);
    }

    @Test
    public void cGetIdCard() throws Exception {
        
        updateIdCardInCurrentSession(status().isOk(), SUCCESS);
        
        getIdCard(SUCCESS, Mappings.A_NEEDED_REG_STEP_1);
        
        postRegistration(FILE_USER_1, status().isOk(), SUCCESS);
        
        getIdCard(SUCCESS, Mappings.A_NEEDED_REG_STEP_3);
        
        postRegion(status().isOk(), SUCCESS);
        
        getIdCard(SUCCESS, Mappings.A_VALID);
    }
    
    @Test
    public void dCheckForDublicateIdCard() throws Exception {
        login(LOGIN_2, PASSWORD_2);
        
        updateIdCardInCurrentSession(status().isConflict(), ALREADY_REGISTERED);
    }

    private void getIdCard(String expectedCode, String expectedAction) throws Exception {
        String endpoint = Mappings.IDCARD;

        ResultActions result = mvc.perform(get(endpoint)
                .session(sessionHolder.getSession()))
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(
                "application/json;charset=UTF-8"));

        String response = result.andReturn().getResponse().getContentAsString();

        log.info(MessageFormat.format("Content of {0} -> {1}",
                endpoint,
                response));

        validateJsonResponse(response, expectedCode, null, expectedAction);
    }
}
