package com.example.demo.servicetest;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.serviceImpl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp(){
        admin = new Admin();
        admin.setPassword("Hello");
        admin.setUsername("abhi07");
        adminService = new AdminServiceImpl(adminRepository,encoder,userRepo);
    }

    @Test
    public void addAdmin(){
        when(userRepo.findByUsername("abhi07")).thenReturn(Optional.empty());
        when(adminRepository.save(admin)).thenReturn(admin);
        Admin admin1 = adminService.addAdmin(admin);
        assertNotNull(admin1);
        assertEquals("abhi07", admin1.getUsername());
    }
}
