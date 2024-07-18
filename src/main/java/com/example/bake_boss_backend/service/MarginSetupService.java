package com.example.bake_boss_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.MarginSetup;
import com.example.bake_boss_backend.repository.MarginSetupRepository;

import jakarta.transaction.Transactional;

@Service
public class MarginSetupService {
    @Autowired
    private MarginSetupRepository marginSetupRepository;

    public MarginSetup saveOrUpdateMarginSetup(MarginSetup marginSetup) {
        Optional<MarginSetup> existingMarginSetup = marginSetupRepository.findByUsernameAndProductName(marginSetup.getUsername(), marginSetup.getProductName());

        if (existingMarginSetup.isPresent()) {
            MarginSetup existing = existingMarginSetup.get();
            existing.setDpMargin(marginSetup.getDpMargin());
            existing.setRpMargin(marginSetup.getRpMargin());
            return marginSetupRepository.save(existing);
        } else {
            return marginSetupRepository.save(marginSetup);
        }
    }

    public MarginSetup getMarginSetupByUsername(String username) {
        return marginSetupRepository.findByUsername(username);
    }
}
