package ru.nsk.test.cabinet;

import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * Base class to derive concrete web test classes from.
 *
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        loader = WebContextLoader.class,
        value = {
    "classpath:**/dispatcherServlet-servlet.xml",
    "classpath:**/applicationContext.xml",
    "classpath:**/test-context.xml",
    "classpath:**/security.xml"
})
public abstract class AbstractWebIntegrationTest extends TestCase {

    @Autowired
    protected WebApplicationContext context;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    protected MockMvc mvc;
    protected SessionHolder sessionHolder = new SessionHolder();
    protected ResultHandler sessionHandler;
    public final static String SUCCESS = "SUCCESS";
    public final static String ALREADY_REGISTERED = "ALREADY_REGISTERED";

    @Before
    @Override
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).
                addFilter(new ShallowEtagHeaderFilter()).
                addFilter(springSecurityFilterChain).
                build();

        sessionHandler = new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                sessionHolder.setSession(new SessionWrapper(result.getRequest().getSession()));
            }
        };
    }

    protected void login(String login, String password) throws Exception {
        String endpoint = Mappings.SPRING_SECURE_LOGIN;

        ResultActions result = mvc.perform(post(endpoint)
                .contentType(MediaType.TEXT_PLAIN)
                .param("j_username", login)
                .param("j_password", password))
                .andExpect(status().isMovedTemporarily())
                .andDo(sessionHandler); // save session

        log.info(MessageFormat.format("Content of {0} -> {1}",
                endpoint,
                result.andReturn().getResponse().getContentAsString()));

        String redirect = result.andReturn().getResponse().getRedirectedUrl();
        if (redirect != null) {
            log.debug("Got redirect: load next url {}", redirect);
            result = mvc.perform(get(redirect)
                    .contentType(MediaType.TEXT_PLAIN))
                    .andExpect(status().isOk());

            log.info(MessageFormat.format("Content of {0} -> {1}",
                    endpoint,
                    result.andReturn().getResponse().getContentAsString()));
        }

    }

    protected String load(String endpoint,
            String contentType,
            ResultMatcher excpectedCode,
            String expectedStatus,
            String expectedDescription) {
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON.toString();
        }
        String response = null;
        try {

            ResultActions result = mvc.perform(get(endpoint)
                    .session(sessionHolder.getSession())).//
                    andExpect(excpectedCode);
            if (contentType.length() > 0) {
                result.andExpect(content().contentType(contentType));
            }

            response = result.andReturn().getResponse().getContentAsString();

            log.info(MessageFormat.format("Content of / -> {0}",
                    response));

            if (expectedStatus != null) {
                validateJsonResponse(
                        response,
                        expectedStatus, expectedDescription, null);
            }
        } catch (Exception e) {
            fail(e);
        }
        return response;
    }

    protected void register(String login, String password, ResultMatcher expectedStatus, String expectedCode) throws Exception {

        String endpoint = Mappings.AUTH_REGISTER;

        ResultActions result = mvc.perform(post(endpoint)
                .contentType(MediaType.TEXT_PLAIN)
                .param("j_username", login)
                .param("j_password", password))
                .andExpect(expectedStatus) //
                .andExpect(content().contentType(
                "application/json;charset=UTF-8"));

        String response = result.andReturn().getResponse().getContentAsString();

        log.info(MessageFormat.format("Content of {0} -> {1}",
                endpoint,
                response));

        validateJsonResponse(response, expectedCode, null, null);
    }

    protected void postRegistration(String file, ResultMatcher expectedStatus, String expectedCode) throws Exception {
        postData(file, Mappings.USER, expectedStatus, expectedCode);
    }

    protected void postRegion(ResultMatcher expectedStatus, String expectedCode) throws Exception {
        postData("region.json", Mappings.PLACE, expectedStatus, expectedCode);
    }

    protected List checkResponseByRegexp(String response, String regexp, int controlValueGroup, int expectedCount) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(response);
        int counter = 0;
        ArrayList<String> result = new ArrayList();

        while (m.find()) {
            String group = m.group();
            log.info("Response control group {}", group);
            counter++;
            result.add(m.group(controlValueGroup));
        }
        TestCase.assertEquals("Filter return invalid count of records",
                expectedCount, counter);
        return result;

    }

    protected void deteteEntity(String path,
            Long id,
            ResultMatcher expectedResult,
            String expectedCode,
            String expectedMessage) {
        try {
            ResultActions result = mvc.perform(delete(path + "/" + id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(expectedResult)
                    .andExpect(content().contentType("application/json;charset=UTF-8"));


            validateJsonResponse(
                    result.andReturn().getResponse().getContentAsString(),
                    expectedCode, expectedMessage, null);

        } catch (Exception e) {
            fail(e);
        }
    }

    protected byte[] loadResource(String file) throws IOException {
        ClassPathResource resource = new ClassPathResource(
                file);
        return Files.readAllBytes(resource.getFile().toPath());

    }

    protected void fail(Exception e) {
        log.error(e.getMessage(), e);
        fail(e.getMessage());
    }

    protected void validateJsonResponse(String response,
            String expectedStatus,
            String expectedDescription,
            String expectedAction) {
        Pattern p = Pattern.compile(
                "\\{\"status\":\"(?<code>[\\w]+)\",\"description\":\"(?<descr>[\\w\\s\\.]+)\"([@\\.,:\\w\\W\\s\"\\{\\}]+)\"action\":\"(?<action>[\\w]*)\"");
        Matcher m = p.matcher(response);
        if (!m.find()) {
            fail(MessageFormat.format(
                    "Cannot match status and description in response [{0}].",
                    response));
        }

        assertEquals(expectedStatus, m.group("code"));
        if (expectedDescription != null) {
            assertEquals(expectedDescription, m.group("descr"));
        }
        if (expectedAction != null) {
            assertEquals(expectedAction, m.group("action"));
        }
    }

    private void postData(String file, String endpoint, ResultMatcher expectedStatus, String expectedCode) throws Exception {

        ClassPathResource resource = new ClassPathResource(
                file);
        byte[] data = Files.readAllBytes(resource.getFile().toPath());

        ResultActions result = mvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .session(sessionHolder.getSession())
                .content(data))
                .andExpect(expectedStatus) //
                .andExpect(content().contentType(
                "application/json;charset=UTF-8"));

        String response = result.andReturn().getResponse().getContentAsString();

        log.info(MessageFormat.format("Content of {0} -> {1}",
                endpoint,
                response));

        validateJsonResponse(response, expectedCode, null, null);
    }
}
