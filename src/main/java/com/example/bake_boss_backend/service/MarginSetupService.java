package com.example.bake_boss_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.MarginSetup;
import com.example.bake_boss_backend.repository.MarginSetupRepository;

import jakarta.transaction.Transactional;

@Service
public class MarginSetupService {
    @Autowired
    private MarginSetupRepository marginSetupRepository;

    @Transactional
    public MarginSetup upsertMarginSetup(String username, Double dpMargin, Double rpMargin) {
        MarginSetup existingSetup = marginSetupRepository.findByUsername(username);
        if (existingSetup != null) {
            existingSetup.setDpMargin(dpMargin);
            existingSetup.setRpMargin(rpMargin);
            return marginSetupRepository.save(existingSetup);
        } else {
            MarginSetup newMarginSetup = new MarginSetup();
            newMarginSetup.setUsername(username);
            newMarginSetup.setDpMargin(dpMargin);
            newMarginSetup.setRpMargin(rpMargin);
            return marginSetupRepository.save(newMarginSetup);
        }
    }

    public MarginSetup getMarginSetupByUsername(String username) {
        return marginSetupRepository.findByUsername(username);
    }
}
