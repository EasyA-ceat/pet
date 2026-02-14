package com.pet.management.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.management.model.Photo;
import com.pet.management.repository.PhotoRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
@Transactional
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    // 照片存储目录
    private static final String PHOTO_UPLOAD_DIR = System.getProperty("user.home") + "/.pet-management-system/photos/";

    public PhotoService() {
        // 确保上传目录存在
        File uploadDir = new File(PHOTO_UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

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
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            // 删除文件
            File file = new File(photo.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            // 删除缩略图
            String thumbnailPath = PHOTO_UPLOAD_DIR + "thumb_" + photo.getFileName();
            File thumbnailFile = new File(thumbnailPath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            // 删除数据库记录
            photoRepository.deleteById(id);
        }
    }

    public List<Photo> findByCustomerId(Long customerId) {
        return photoRepository.findByCustomer_Id(customerId);
    }

    public List<Photo> findByTransactionId(Long transactionId) {
        return photoRepository.findByTransaction_Id(transactionId);
    }

    public List<Photo> findByCustomerIdAndPhotoType(Long customerId, String photoType) {
        return photoRepository.findByCustomer_IdAndPhotoType(customerId, photoType);
    }

    public List<Photo> findByTransactionIdAndPhotoType(Long transactionId, String photoType) {
        return photoRepository.findByTransaction_IdAndPhotoType(transactionId, photoType);
    }

    /**
     * 上传照片并保存到数据库
     * @param file 上传的文件
     * @param customerId 顾客ID
     * @param transactionId 交易ID（可选）
     * @param photoType 照片类型（before/after/other）
     * @param notes 备注
     * @return 保存后的Photo对象
     * @throws IOException 上传失败时抛出异常
     */
    public Photo uploadPhoto(File file, Long customerId, Long transactionId, String photoType, String notes) throws IOException {
        // 生成唯一文件名
        String originalFileName = file.getName();
        String fileName = System.currentTimeMillis() + "_" + originalFileName;
        Path targetLocation = Paths.get(PHOTO_UPLOAD_DIR + fileName);

        // 保存原始文件
        Files.copy(file.toPath(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 生成缩略图
        String thumbnailFileName = "thumb_" + fileName;
        Path thumbnailLocation = Paths.get(PHOTO_UPLOAD_DIR + thumbnailFileName);
        Thumbnails.of(targetLocation.toFile())
                .size(200, 200)
                .outputQuality(0.8)
                .toFile(thumbnailLocation.toFile());

        // 创建Photo对象
        Photo photo = new Photo();
        photo.setCustomerId(customerId);
        photo.setTransactionId(transactionId);
        photo.setPhotoType(photoType);
        photo.setFilePath(targetLocation.toString());
        photo.setFileName(fileName);
        photo.setNotes(notes);
        photo.setUploadTime(LocalDateTime.now());

        return photoRepository.save(photo);
    }

    /**
     * 删除照片文件和数据库记录
     * @param photoId 照片ID
     */
    public void deletePhoto(Long photoId) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);
        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            // 删除原始文件
            File originalFile = new File(photo.getFilePath());
            if (originalFile.exists()) {
                originalFile.delete();
            }
            // 删除缩略图
            String thumbnailPath = PHOTO_UPLOAD_DIR + "thumb_" + photo.getFileName();
            File thumbnailFile = new File(thumbnailPath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            // 删除数据库记录
            photoRepository.deleteById(photoId);
        }
    }

    /**
     * 获取缩略图路径
     * @param photo 照片对象
     * @return 缩略图路径
     */
    public String getThumbnailPath(Photo photo) {
        return PHOTO_UPLOAD_DIR + "thumb_" + photo.getFileName();
    }

    /**
     * 检查照片文件是否存在
     * @param photo 照片对象
     * @return 文件是否存在
     */
    public boolean isPhotoFileExists(Photo photo) {
        return new File(photo.getFilePath()).exists();
    }
}
