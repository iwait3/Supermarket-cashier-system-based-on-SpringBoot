package com.lab.controller;

import com.lab.entity.Cart;
import com.lab.entity.Order;
import com.lab.entity.OrderItem;
import com.lab.entity.Product;
import com.lab.repository.OrderItemRepository;
import com.lab.service.CartService;
import com.lab.service.OrderService;
import com.lab.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;
import java.util.Date;

@Controller
@RequestMapping("/admin/cashier")
public class CashierController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * 收银页面
     */
    @GetMapping("")
    public String cashier(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // 查找所有商品
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        // 查找购物车
        List<Cart> carts = cartService.findAll();
        model.addAttribute("carts", carts);

        // 计算总金额
        double total = 0;
        for (Cart cart : carts) {
            total += cart.getProduct().getPrice() * cart.getQuantity();
        }
        model.addAttribute("total", total);

        return "cashier";
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Product product = productService.findById(productId);
        if (product == null) {
            return "redirect:/admin/cashier";
        }

        // 检查购物车中是否已有该商品
        Cart cart = cartService.findByProduct(product);
        if (cart != null) {
            // 如果已有，增加数量
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            // 如果没有，创建新购物车项
            cart = new Cart();
            cart.setProduct(product);
            cart.setQuantity(quantity);
        }

        cartService.save(cart);
        return "redirect:/admin/cashier";
    }

    /**
     * 删除购物车项
     */
    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        cartService.delete(id);
        return "redirect:/admin/cashier";
    }

    /**
     * 更新购物车项数量
     */
    @PostMapping("/update-cart")
    public String updateCart(@RequestParam Long id, @RequestParam int quantity, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // 查找购物车项
        List<Cart> carts = cartService.findAll();
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getId().equals(id)) {
                cart = c;
                break;
            }
        }

        if (cart != null) {
            cart.setQuantity(quantity);
            cartService.save(cart);
        }
        return "redirect:/admin/cashier";
    }

    /**
     * 清空购物车
     */
    @GetMapping("/clear-cart")
    public String clearCart(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        cartService.deleteAll();
        return "redirect:/admin/cashier";
    }

    /**
     * 生成订单
     */
    @PostMapping("/create-order")
    public String createOrder(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // 获取购物车
        List<Cart> carts = cartService.findAll();
        if (carts.isEmpty()) {
            return "redirect:/admin/cashier";
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderDate(new Date());
        order.setStatus("已支付"); // 收银系统直接标记为已支付

        // 计算总金额
        double totalPrice = 0;
        List<OrderItem> orderItems = new java.util.ArrayList<>();

        for (Cart cart : carts) {
            Product product = cart.getProduct();
            if (product.getStock() < cart.getQuantity()) {
                // 库存不足
                return "redirect:/admin/cashier?error=库存不足";
            }

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItems.add(orderItem);

            // 计算总金额
            totalPrice += product.getPrice() * cart.getQuantity();

            // 减少库存
            product.setStock(product.getStock() - cart.getQuantity());
            productService.save(product);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        // 先保存订单
        Order savedOrder = orderService.save(order);

        // 保存每个订单项
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        // 清空购物车
        cartService.deleteAll();

        // 跳转到订单成功页面，传递整个order对象
        model.addAttribute("order", savedOrder);
        return "order_success";
    }
}
