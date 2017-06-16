package kuvaldis.play.springframework.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class JspViewReturningController {

    @RequestMapping(value = "jsp/test", method = RequestMethod.GET)
    public String returnJsp() {
        return "test";
    }
}
