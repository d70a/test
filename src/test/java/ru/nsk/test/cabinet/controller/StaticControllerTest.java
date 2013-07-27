package ru.nsk.test.cabinet.controller;

import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.nsk.test.cabinet.AbstractWebIntegrationTest;
import ru.nsk.test.cabinet.Mappings;

/**
 *
 * @author me
 */
public class StaticControllerTest extends AbstractWebIntegrationTest {

    @Test
    public void simpleTest() throws Exception {
        mvc.perform(get(Mappings.ROOT)).
                andExpect(status().isOk());
    }
}
