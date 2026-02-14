package com.pet.management.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.pet.management.model.Clerk;
import com.pet.management.repository.ClerkRepository;

class ClerkServiceTest {

    @Mock
    private ClerkRepository clerkRepository;

    @InjectMocks
    private ClerkService clerkService;

    private List<Clerk> mockClerks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 创建一些测试数据
        mockClerks = new ArrayList<>();
        
        Clerk clerk1 = new Clerk();
        clerk1.setId(1L);
        clerk1.setClerkName("张三");
        clerk1.setPhone("13812345678");
        clerk1.setCommissionRate(BigDecimal.valueOf(0.05));
        clerk1.setNotes("优秀员工");
        mockClerks.add(clerk1);
        
        Clerk clerk2 = new Clerk();
        clerk2.setId(2L);
        clerk2.setClerkName("李四");
        clerk2.setPhone("13912345678");
        clerk2.setCommissionRate(BigDecimal.valueOf(0.06));
        clerk2.setNotes("经验丰富");
        mockClerks.add(clerk2);
        
        Clerk clerk3 = new Clerk();
        clerk3.setId(3L);
        clerk3.setClerkName("王五");
        clerk3.setPhone("13612345678");
        clerk3.setCommissionRate(BigDecimal.valueOf(0.04));
        clerk3.setNotes("新员工");
        mockClerks.add(clerk3);
    }

    @Test
    void testFindAll() {
        when(clerkRepository.findAll()).thenReturn(mockClerks);
        
        List<Clerk> result = clerkService.findAll();
        
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(clerkRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Long id = 1L;
        when(clerkRepository.findById(id)).thenReturn(Optional.of(mockClerks.get(0)));
        
        Optional<Clerk> result = clerkService.findById(id);
        
        assertNotNull(result);
        assertEquals("张三", result.get().getClerkName());
        verify(clerkRepository, times(1)).findById(id);
    }

    @Test
    void testSave() {
        Clerk newClerk = new Clerk();
        newClerk.setClerkName("赵六");
        newClerk.setPhone("13712345678");
        newClerk.setCommissionRate(BigDecimal.valueOf(0.07));
        newClerk.setNotes("销售冠军");
        
        when(clerkRepository.save(newClerk)).thenReturn(newClerk);
        
        Clerk result = clerkService.save(newClerk);
        
        assertNotNull(result);
        assertEquals("赵六", result.getClerkName());
        verify(clerkRepository, times(1)).save(newClerk);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        
        clerkService.deleteById(id);
        
        verify(clerkRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByClerkNameContaining() {
        String name = "张";
        List<Clerk> filteredClerks = new ArrayList<>();
        filteredClerks.add(mockClerks.get(0));
        
        when(clerkRepository.findByClerkNameContaining(name)).thenReturn(filteredClerks);
        
        List<Clerk> result = clerkService.findByClerkNameContaining(name);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("张三", result.get(0).getClerkName());
        verify(clerkRepository, times(1)).findByClerkNameContaining(name);
    }

    @Test
    void testFindByPhoneContaining() {
        String phone = "138";
        List<Clerk> filteredClerks = new ArrayList<>();
        filteredClerks.add(mockClerks.get(0));
        
        when(clerkRepository.findByPhoneContaining(phone)).thenReturn(filteredClerks);
        
        List<Clerk> result = clerkService.findByPhoneContaining(phone);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("13812345678", result.get(0).getPhone());
        verify(clerkRepository, times(1)).findByPhoneContaining(phone);
    }

    @Test
    void testFindByCommissionRateGreaterThan() {
        Double rate = 0.05;
        List<Clerk> filteredClerks = new ArrayList<>();
        filteredClerks.add(mockClerks.get(1));
        
        when(clerkRepository.findByCommissionRateGreaterThan(rate)).thenReturn(filteredClerks);
        
        List<Clerk> result = clerkService.findByCommissionRateGreaterThan(rate);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(0.06), result.get(0).getCommissionRate());
        verify(clerkRepository, times(1)).findByCommissionRateGreaterThan(rate);
    }
}
