package com.pet.management.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "photo_type", nullable = false, length = 20)
    private String photoType; // before, after, other

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_name", length = 200)
    private String fileName;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public Photo() {
        this.uploadTime = LocalDateTime.now();
    }

    public Photo(Customer customer, Transaction transaction, String photoType, String filePath, String fileName) {
        this.customer = customer;
        this.transaction = transaction;
        this.photoType = photoType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uploadTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // 设置顾客ID（用于简化操作）
    public void setCustomerId(Long customerId) {
        if (customerId != null) {
            this.customer = new Customer();
            this.customer.setId(customerId);
        } else {
            this.customer = null;
        }
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    // 设置交易ID（用于简化操作）
    public void setTransactionId(Long transactionId) {
        if (transactionId != null) {
            this.transaction = new Transaction();
            this.transaction.setId(transactionId);
        } else {
            this.transaction = null;
        }
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
