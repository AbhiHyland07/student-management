package com.example.demo.service.serviceImpl;

import com.example.demo.exception.model.ResourceAlreadyPresent;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.model.Admin;
import com.example.demo.model.UserName;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder encoder,
                            UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public Admin addAdmin(Admin admin) {
        Optional<UserName> userNameOptional = userRepository.findByUsername(admin.getUsername());
        if (userNameOptional.isPresent()){
            throw new ResourceAlreadyPresent("The username with "+userNameOptional.get().getUsername()+" is present.");
        }
        admin.setPassword(encoder.encode(admin.getPassword()));
        UserName userName = new UserName();
        userName.setUsername(admin.getUsername());
        userRepository.save(userName);
        adminRepository.save(admin);
        return admin;
    }
}
