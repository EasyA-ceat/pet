package com.pet.management.service;

import com.pet.management.model.Clerk;
import com.pet.management.repository.ClerkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClerkService {

    @Autowired
    private ClerkRepository clerkRepository;

    public List<Clerk> findAll() {
        return clerkRepository.findAll();
    }

    public Page<Clerk> findAll(Pageable pageable) {
        return clerkRepository.findAll(pageable);
    }

    public Optional<Clerk> findById(Long id) {
        return clerkRepository.findById(id);
    }

    public Clerk save(Clerk clerk) {
        return clerkRepository.save(clerk);
    }

    public void deleteById(Long id) {
        clerkRepository.deleteById(id);
    }

    public List<Clerk> findByClerkNameContaining(String clerkName) {
        return clerkRepository.findByClerkNameContaining(clerkName);
    }

    public List<Clerk> findByPhoneContaining(String phone) {
        return clerkRepository.findByPhoneContaining(phone);
    }

    public List<Clerk> findByCommissionRateGreaterThan(Double rate) {
        return clerkRepository.findByCommissionRateGreaterThan(rate);
    }
}
