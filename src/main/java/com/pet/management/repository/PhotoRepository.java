package com.pet.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pet.management.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

    List<Photo> findByCustomer_Id(Long customerId);

    List<Photo> findByTransaction_Id(Long transactionId);

    List<Photo> findByCustomer_IdAndPhotoType(Long customerId, String photoType);

    List<Photo> findByTransaction_IdAndPhotoType(Long transactionId, String photoType);
}
