package com.pet.management.service;

import com.pet.management.model.Photo;
import com.pet.management.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    public Optional<Photo> findById(Long id) {
        return photoRepository.findById(id);
    }

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }

    public List<Photo> findByCustomerId(Long customerId) {
        return photoRepository.findByCustomerId(customerId);
    }

    public List<Photo> findByTransactionId(Long transactionId) {
        return photoRepository.findByTransactionId(transactionId);
    }

    public List<Photo> findByCustomerIdAndPhotoType(Long customerId, String photoType) {
        return photoRepository.findByCustomerIdAndPhotoType(customerId, photoType);
    }

    public List<Photo> findByTransactionIdAndPhotoType(Long transactionId, String photoType) {
        return photoRepository.findByTransactionIdAndPhotoType(transactionId, photoType);
    }
}
