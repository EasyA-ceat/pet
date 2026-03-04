package com.pet.management.repository;

import com.pet.management.model.ClerkServiceCommission;
import com.pet.management.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClerkServiceCommissionRepository extends JpaRepository<ClerkServiceCommission, Long> {

    List<ClerkServiceCommission> findByClerkId(Long clerkId);

    Optional<ClerkServiceCommission> findByClerkIdAndServiceType(Long clerkId, ServiceType serviceType);

    void deleteByClerkId(Long clerkId);
}
