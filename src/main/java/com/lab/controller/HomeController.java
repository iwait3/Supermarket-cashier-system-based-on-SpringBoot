package com.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    /**
     * 首页（重定向到登录）
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
