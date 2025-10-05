package com.example.Dailydone.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class Checker {
    @GetMapping
    public String ShowText() {
        return "checking of api";
    }


    @GetMapping
    public String showSomething(){
        return "checking pit";
    }

    @GetMapping
    public String showtSomething(){
        return "checking git";
    }

}
