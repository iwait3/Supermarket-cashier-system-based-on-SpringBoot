package com.lab.service;

import com.lab.entity.Order;
import com.lab.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public Order save(Order order) {
        return orderRepository.save(order);
    }


    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public List<Order> findAll() {
        return orderRepository.findAll();
    }


    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}