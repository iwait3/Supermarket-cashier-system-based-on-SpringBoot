package com.lab.entity;

import jakarta.persistence.*;

//临时购物车，用于创建订单
@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id",nullable=false)
    private Product product;

    @Column(nullable = false)
    private int quantity;
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }

    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product=product;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
}
