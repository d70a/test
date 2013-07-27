package ru.nsk.test.cabinet.controller;

import java.text.MessageFormat;
import javax.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.nsk.test.cabinet.Mappings;

/**
 * Static controller maps root of project static content to main index.jsp file.
 *
 */
@Controller
@Slf4j
public class StaticContentController {

    @RequestMapping(value = Mappings.ROOT, method = RequestMethod.GET)
    public String indexRoot(ServletRequest request) {
        
        log.info(MessageFormat.format("Controller route {0} to /", 
                request));
        
        return "index";
    }
}
