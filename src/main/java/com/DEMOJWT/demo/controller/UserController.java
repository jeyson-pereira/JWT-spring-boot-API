package com.DEMOJWT.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DEMOJWT.demo.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String pwd) {
        return service.login(username, pwd);
    }

    @PostMapping("user/register")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String pwd) {
        return service.registerUser(username, pwd);
    }
}
