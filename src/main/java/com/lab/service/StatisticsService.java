package com.lab.service;

import com.lab.entity.Order;
import com.lab.entity.OrderItem;
import com.lab.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;
    public Map<String, Integer> getSalesStatistics() {
        List<Order> orders = orderService.findAll();
        Map<Long, Integer> productSales = new HashMap<>();

        for (Order order : orders) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    Long productId = item.getProduct().getId();
                    int quantity = item.getQuantity();
                    productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
                }
            }
        }

        Map<String, Integer> salesMap = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : productSales.entrySet()) {
            Product product = productService.findById(entry.getKey());
            if (product != null) {
                salesMap.put(product.getName(), entry.getValue());
            }
        }

        // 如果没有数据，添加默认数据
        if (salesMap.isEmpty()) {
            salesMap.put("暂无数据", 0);
        }

        return salesMap;
    }

    /**
     * 获取盈利统计数据
     */
    public Map<String, Double> getProfitStatistics() {
        List<Order> orders = orderService.findAll();
        Map<Long, Double> productProfit = new HashMap<>();

        // 统计每个商品的盈利
        for (Order order : orders) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    Long productId = item.getProduct().getId();
                    double profit = item.getPrice() * item.getQuantity();
                    productProfit.put(productId, productProfit.getOrDefault(productId, 0.0) + profit);
                }
            }
        }

        // 转换为商品名称和盈利的映射
        Map<String, Double> profitMap = new HashMap<>();
        for (Map.Entry<Long, Double> entry : productProfit.entrySet()) {
            Product product = productService.findById(entry.getKey());
            if (product != null) {
                profitMap.put(product.getName(), entry.getValue());
            }
        }

        // 如果没有数据，添加默认数据
        if (profitMap.isEmpty()) {
            profitMap.put("暂无数据", 0.0);
        }

        return profitMap;
    }

    /**
     * 获取总销售额
     */
    public double getTotalSales() {
        List<Order> orders = orderService.findAll();
        return orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }


    public int getOrderCount() {
        return orderService.findAll().size();
    }
}