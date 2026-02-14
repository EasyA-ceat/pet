package com.pet.management.repository;

import com.pet.management.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

    List<Photo> findByCustomerId(Long customerId);

    List<Photo> findByTransactionId(Long transactionId);

    List<Photo> findByCustomerIdAndPhotoType(Long customerId, String photoType);

    List<Photo> findByTransactionIdAndPhotoType(Long transactionId, String photoType);
}
