package com.lab.repository;

import com.lab.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
    // 根据用户名查找管理员
    Admin findByUsername(String username);
}
