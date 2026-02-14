package com.pet.management.repository;

import com.pet.management.model.Clerk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClerkRepository extends JpaRepository<Clerk, Long>, JpaSpecificationExecutor<Clerk> {

    List<Clerk> findByClerkNameContaining(String clerkName);

    List<Clerk> findByPhoneContaining(String phone);

    List<Clerk> findByCommissionRateGreaterThan(Double rate);
}
