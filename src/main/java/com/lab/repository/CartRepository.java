package com.lab.repository;

import com.lab.entity.Cart;
import com.lab.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {
    // 根据商品查找购物车
    Cart findByProduct(Product product);
}
