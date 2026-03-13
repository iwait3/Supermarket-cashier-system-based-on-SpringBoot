package com.lab.controller;

import com.lab.entity.Order;
import com.lab.service.OrderService;
import com.lab.service.StatisticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/orders")
public class OrderManagementController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 订单列表
     */
    @GetMapping("")
    public String orderList(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    /**
     * 订单详情
     */
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Order order = orderService.findById(id);
        if (order == null) {
            return "redirect:/admin/orders";
        }

        model.addAttribute("order", order);
        return "admin/order_detail";
    }

    /**
     * 删除订单
     */
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        orderService.delete(id);
        return "redirect:/admin/orders";
    }

    /**
     * 更新订单状态
     */
    @PostMapping("/update")
    public String updateOrderStatus(@RequestParam Long id, @RequestParam String status, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Order order = orderService.findById(id);
        if (order != null) {
            order.setStatus(status);
            orderService.save(order);
        }
        return "redirect:/admin/orders/detail/" + id;
    }

    /**
     * 统计页面
     */
    @GetMapping("/statistics")
    public String statistics(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // 获取销量统计数据
        Map<String, Integer> salesData = statisticsService.getSalesStatistics();
        model.addAttribute("salesData", salesData);

        // 获取盈利统计数据
        Map<String, Double> profitData = statisticsService.getProfitStatistics();
        model.addAttribute("profitData", profitData);

        // 获取总销售额和订单数
        double totalSales = statisticsService.getTotalSales();
        int orderCount = statisticsService.getOrderCount();
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("orderCount", orderCount);

        return "admin/statistics";
    }
}