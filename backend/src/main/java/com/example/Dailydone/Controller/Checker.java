package com.example.Dailydone.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checking")
public class Checker {

    private static final Logger log = LoggerFactory.getLogger(Checker.class);

    @GetMapping
    public String ShowText() {
        log.info("how does logs appear in terminal");
        return "checking of api";
    }

    @GetMapping("/dosomething")
    public String showtSomething(){
        return "checking git";
    }

}
