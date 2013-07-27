package ru.nsk.test.cabinet.controller;

import static junit.framework.TestCase.assertEquals;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ru.nsk.test.cabinet.AbstractWebIntegrationTest;
import ru.nsk.test.cabinet.pojo.User;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.nsk.test.cabinet.AbstractWebIntegrationTest.SUCCESS;

/**
 *
 * @author me
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest extends AbstractWebIntegrationTest {

    private final static String LOGIN_1 = "testuser2@dev.com";
    private final static String PASSWORD_1 = "test";
    private final static String FILE_1 = "user_2.json";
    private final static String LOGIN_2 = "testuser3@dev.com";
    private final static String PASSWORD_2 = "test";
    private final static String FILE_2 = "user_3.json";

    @Test
    public void aRegister() throws Exception {
        /**
         * Login to system (create session)
         */
        register(LOGIN_1, PASSWORD_1, status().isOk(), SUCCESS);
        register(LOGIN_2, PASSWORD_2, status().isOk(), SUCCESS);

    }

    @Test
    public void bTestUniqueFields() throws Exception {
        login(LOGIN_1, PASSWORD_1);
        postRegistration(FILE_1, status().isOk(), SUCCESS);
        login(LOGIN_2, PASSWORD_2);
        postRegistration(FILE_2, status().isConflict(), ALREADY_REGISTERED);
    }

    @Test
    public void zCombineAndPhoneDecodeTest() {
        String[] phones = new String[]{
            "+79131234567", "9131234567", "+7-913-1234567",
            "+7(913)1234567", "8-913-123-45-67", "913-123-45-67"};
        UserController instance = new UserController();
        User dst = new User(LOGIN_1, PASSWORD_1);
        User src = new User(null, null);
        src.setPassword(dst.getPassword());


        for (String phone : phones) {
            src.setPhone(phone);

            instance.combine(dst, src);
            assertEquals("Phone code not match", dst.getCode(), "913");
            assertEquals("Phone number not match", dst.getPhone(), "1234567");
            assertEquals("E-mail broken", LOGIN_1,
                    dst.getEmail());
            assertEquals("Password broken", src.getPassword(),
                    dst.getPassword());
        }
    }
}
