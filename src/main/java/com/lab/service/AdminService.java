package com.lab.service;

import com.lab.entity.Admin;
import com.lab.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin findById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }


    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }


    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}
