package lv.datuskola.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;

@Controller
public class AController {

    @GetMapping(value="/test")
    @Transactional
    public String abc() {
        return "blank";
    }
}
