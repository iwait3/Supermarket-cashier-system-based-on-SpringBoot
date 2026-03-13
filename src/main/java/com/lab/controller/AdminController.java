package com.lab.controller;

import com.lab.entity.Admin;
import com.lab.service.AdminService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     * 验证用户名和密码，登录成功后跳转到收银页面
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Admin admin = adminService.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/cashier";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    /**
     * 管理员登出
     * 清除session并返回登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}