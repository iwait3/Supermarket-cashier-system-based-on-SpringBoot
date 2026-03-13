package com.lab.service;

import com.lab.entity.Product;
import com.lab.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}