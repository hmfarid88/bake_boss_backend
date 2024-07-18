package com.example.bake_boss_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bake_boss_backend.entity.CustomerInfo;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long>{

    Optional<CustomerInfo> findBySoldInvoice(String soldInvoice);
    
}
