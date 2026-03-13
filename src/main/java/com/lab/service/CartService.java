package com.lab.service;

import com.lab.entity.Cart;
import com.lab.entity.Product;
import com.lab.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;


    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }


    public List<Cart> findAll() {
        return cartRepository.findAll();
    }


    public Cart findByProduct(Product product) {
        return cartRepository.findByProduct(product);
    }


    public void delete(Long id) {
        cartRepository.deleteById(id);
    }


    public void deleteAll() {
        cartRepository.deleteAll();
    }
}