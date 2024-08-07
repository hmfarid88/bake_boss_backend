package com.example.bake_boss_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.repository.ItemMakeRepository;

@Service
public class ItemMakeService {
    @Autowired
    private ItemMakeRepository itemMakeRepository;

    public List<Object[]> getMaterialsAndQtyGroupedByItemName(String username) {
        return itemMakeRepository.findMaterialsAndQtyGroupedByItemName(username);
    }
}
