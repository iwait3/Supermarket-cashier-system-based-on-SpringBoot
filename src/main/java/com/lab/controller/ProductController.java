package com.lab.controller;

import com.lab.entity.Product;
import com.lab.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 商品管理页面
     */
    @GetMapping("/list")
    public String productList(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "admin/products";
    }

    /**
     * 添加商品页面
     */
    @GetMapping("/add")
    public String addProductPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        model.addAttribute("product", new Product());
        return "admin/product_add";
    }

    /**
     * 添加商品（表单提交）
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        productService.save(product);
        model.addAttribute("product", product);
        model.addAttribute("action", "添加");
        return "product_success";
    }

    /**
     * 编辑商品页面
     */
    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/list";
        }

        model.addAttribute("product", product);
        return "admin/product_edit";
    }

    /**
     * 编辑商品（表单提交）
     */
    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        productService.save(product);
        model.addAttribute("product", product);
        model.addAttribute("action", "编辑");
        return "product_success";
    }

    /**
     * 删除商品
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Product product = productService.findById(id);
        if (product != null) {
            model.addAttribute("product", product);
            productService.delete(id);
            model.addAttribute("action", "删除");
            return "product_success";
        }
        return "redirect:/admin/product/list";
    }
}
