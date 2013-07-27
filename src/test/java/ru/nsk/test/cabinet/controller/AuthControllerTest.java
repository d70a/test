/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.nsk.test.cabinet.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ru.nsk.test.cabinet.AbstractWebIntegrationTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import ru.nsk.test.cabinet.Mappings;

/**
 *
 * @author me
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthControllerTest extends AbstractWebIntegrationTest {

    private final static String LOGIN = "2@2.com";
    private final static String PASSWORD = "test";

    public AuthControllerTest() {
    }

    @Test
    public void aTestLoginByNonExistingUser() throws Exception {
        login(LOGIN, PASSWORD);
    }

    @Test
    public void bTestRegister() throws Exception {
        register(LOGIN, PASSWORD, status().isOk(), "SUCCESS");
        register(LOGIN, PASSWORD, status().isConflict(), "ALREADY_REGISTERED");
    }
    
    @Test
    public void cSimplyGetAuth() throws Exception {
        load(Mappings.AUTH, "", status().isNotFound(), null, null);
        load(Mappings.AUTH_LOGIN, "", status().isOk(), null, null);
    }
}