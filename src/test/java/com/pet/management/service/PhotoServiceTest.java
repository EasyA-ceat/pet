package com.pet.management.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pet.management.model.Photo;
import com.pet.management.repository.PhotoRepository;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    @Test
    void testFindAll() {
        // 模拟数据
        Photo photo1 = new Photo();
        photo1.setId(1L);
        Photo photo2 = new Photo();
        photo2.setId(2L);
        List<Photo> photos = Arrays.asList(photo1, photo2);

        // 模拟repository方法
        when(photoRepository.findAll()).thenReturn(photos);

        // 调用service方法
        List<Photo> result = photoService.findAll();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(photoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // 模拟数据
        Photo photo = new Photo();
        photo.setId(1L);

        // 模拟repository方法
        when(photoRepository.findById(1L)).thenReturn(Optional.of(photo));

        // 调用service方法
        Optional<Photo> result = photoService.findById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(photoRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        // 模拟数据
        Photo photo = new Photo();
        photo.setId(1L);

        // 模拟repository方法
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        // 调用service方法
        Photo result = photoService.save(photo);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    void testDeleteById() {
        // 模拟数据
        Photo photo = new Photo();
        photo.setId(1L);
        photo.setFileName("test.jpg");
        photo.setFilePath(System.getProperty("user.home") + "/.pet-management-system/photos/test.jpg");

        // 创建测试文件
        try {
            Path testFile = Paths.get(photo.getFilePath());
            Files.createDirectories(testFile.getParent());
            Files.createFile(testFile);
            Path thumbnailFile = Paths.get(System.getProperty("user.home") + "/.pet-management-system/photos/thumb_test.jpg");
            Files.createFile(thumbnailFile);
        } catch (IOException e) {
            fail("无法创建测试文件: " + e.getMessage());
        }

        // 模拟repository方法
        when(photoRepository.findById(1L)).thenReturn(Optional.of(photo));

        // 调用service方法
        photoService.deleteById(1L);

        // 验证结果
        verify(photoRepository, times(1)).deleteById(1L);

        // 验证文件是否被删除
        File testFile = new File(photo.getFilePath());
        File thumbnailFile = new File(System.getProperty("user.home") + "/.pet-management-system/photos/thumb_test.jpg");
        assertFalse(testFile.exists());
        assertFalse(thumbnailFile.exists());
    }

    @Test
    void testFindByCustomerId() {
        // 模拟数据
        Photo photo1 = new Photo();
        photo1.setId(1L);
        Photo photo2 = new Photo();
        photo2.setId(2L);
        List<Photo> photos = Arrays.asList(photo1, photo2);

        // 模拟repository方法
        when(photoRepository.findByCustomer_Id(1L)).thenReturn(photos);

        // 调用service方法
        List<Photo> result = photoService.findByCustomerId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(photoRepository, times(1)).findByCustomer_Id(1L);
    }

    @Test
    void testFindByTransactionId() {
        // 模拟数据
        Photo photo1 = new Photo();
        photo1.setId(1L);
        Photo photo2 = new Photo();
        photo2.setId(2L);
        List<Photo> photos = Arrays.asList(photo1, photo2);

        // 模拟repository方法
        when(photoRepository.findByTransaction_Id(1L)).thenReturn(photos);

        // 调用service方法
        List<Photo> result = photoService.findByTransactionId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(photoRepository, times(1)).findByTransaction_Id(1L);
    }

    @Test
    void testFindByCustomerIdAndPhotoType() {
        // 模拟数据
        Photo photo1 = new Photo();
        photo1.setId(1L);
        Photo photo2 = new Photo();
        photo2.setId(2L);
        List<Photo> photos = Arrays.asList(photo1, photo2);

        // 模拟repository方法
        when(photoRepository.findByCustomer_IdAndPhotoType(1L, "before")).thenReturn(photos);

        // 调用service方法
        List<Photo> result = photoService.findByCustomerIdAndPhotoType(1L, "before");

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(photoRepository, times(1)).findByCustomer_IdAndPhotoType(1L, "before");
    }

    @Test
    void testFindByTransactionIdAndPhotoType() {
        // 模拟数据
        Photo photo1 = new Photo();
        photo1.setId(1L);
        Photo photo2 = new Photo();
        photo2.setId(2L);
        List<Photo> photos = Arrays.asList(photo1, photo2);

        // 模拟repository方法
        when(photoRepository.findByTransaction_IdAndPhotoType(1L, "after")).thenReturn(photos);

        // 调用service方法
        List<Photo> result = photoService.findByTransactionIdAndPhotoType(1L, "after");

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(photoRepository, times(1)).findByTransaction_IdAndPhotoType(1L, "after");
    }

    @Test
    void testIsPhotoFileExists() {
        // 模拟数据
        Photo photo = new Photo();
        photo.setFilePath(System.getProperty("user.home") + "/.pet-management-system/photos/test.jpg");

        // 创建测试文件
        try {
            Path testFile = Paths.get(photo.getFilePath());
            Files.createDirectories(testFile.getParent());
            Files.createFile(testFile);
        } catch (IOException e) {
            fail("无法创建测试文件: " + e.getMessage());
        }

        // 调用service方法
        boolean result = photoService.isPhotoFileExists(photo);

        // 验证结果
        assertTrue(result);

        // 删除测试文件
        new File(photo.getFilePath()).delete();
    }

    @Test
    void testGetThumbnailPath() {
        // 模拟数据
        Photo photo = new Photo();
        photo.setFileName("test.jpg");

        // 调用service方法
        String thumbnailPath = photoService.getThumbnailPath(photo);

        // 验证结果
        assertNotNull(thumbnailPath);
        assertTrue(thumbnailPath.contains("thumb_test.jpg"));
    }
}
