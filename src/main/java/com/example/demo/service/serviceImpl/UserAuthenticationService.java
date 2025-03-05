package com.example.demo.service.serviceImpl;

import com.example.demo.model.*;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    @Autowired
    UserAuthenticationService(StudentRepository studentRepository, TeacherRepository teacherRepository, AdminRepository adminRepository){
        this.studentRepository=studentRepository;
        this.teacherRepository=teacherRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Student> student=studentRepository.findByUsername(email);
        Optional<Teacher> teacher=teacherRepository.findByUsername(email);
        Optional<Admin> admin=adminRepository.findByUsername(email);
        List<SimpleGrantedAuthority> roles;
        Role role;
        if (student.isPresent()){
            role=student.get().getRole();
        } else if (teacher.isPresent()) {
            role=teacher.get().getRole();
        } else if (admin.isPresent()) {
            role=admin.get().getRole();
        } else throw new RuntimeException("Invalid Users");
        if(role.equals(Role.STUDENT) && student.isPresent())
        {
            roles = List.of(new SimpleGrantedAuthority(Role.STUDENT.name()));
            return new UserExtend(new User(student.get().getUsername(), student.get().getPassword(),roles),student.get().getId());
        }
        else if(role.equals(Role.TEACHER) && teacher.isPresent())
        {
            roles = List.of(new SimpleGrantedAuthority(Role.TEACHER.name()));
            return new UserExtend(new User(teacher.get().getUsername(), teacher.get().getPassword(),roles),teacher.get().getId());
        }
        else if(role.equals(Role.ADMIN) && admin.isPresent()){
            roles = List.of(new SimpleGrantedAuthority(Role.ADMIN.name()));
            return new UserExtend(new User(admin.get().getUsername(), admin.get().getPassword(),roles),admin.get().getId());
        }
        return null;
    }
}
