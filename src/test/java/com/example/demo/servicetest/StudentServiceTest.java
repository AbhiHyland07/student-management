package com.example.demo.servicetest;


import com.example.demo.model.Courses;
import com.example.demo.model.Student;
import com.example.demo.model.modelDTO.CourseDTO;
import com.example.demo.model.modelDTO.StudentDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.serviceImpl.CourseServiceImpl;
import com.example.demo.service.serviceImpl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private CourseRepository courseRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private StudentServiceImpl service;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Student student;
    private Courses courses;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final ModelMapper mapper = new ModelMapper();

    @BeforeEach
    public void setUp(){
        student = new Student("abhi07","Hello","Abhigyan");
        student.setPassword(encoder.encode("Hello"));
        courses = new Courses();
        courses.setCourseId("2");
        courses.setName("Course");
        courses.setDescription("Course2");
        service = new StudentServiceImpl(studentRepo,courseRepo,encoder,userRepo);
    }
    @Test
    public void testGetUserByUsername() {
        when(studentRepo.findByUsername("abhi07")).thenReturn(Optional.ofNullable(student));
        StudentDTO user = service.getStudent("abhi07");
        System.out.println("Hi");
        assertNotNull(user);
        assertEquals("abhi07",user.getUsername());
    }

    @Test
    public void addCourse() {
        when(studentRepo.findByUsername("abhi07")).thenReturn(Optional.ofNullable(student));
        StudentDTO user = service.getStudent("abhi07");
        when(courseRepo.findByCourseId("2")).thenReturn(Optional.ofNullable(courses));
        CourseDTO courses1 = courseService.getCourse("2");
        assertNotNull(user);
        assertNotNull(courses1);
        assertEquals("abhi07",user.getUsername());
        if (user.getCourses() == null){
            user.setCourses(new ArrayList<>());
        }
        user.getCourses().add(mapper.map(courses1,Courses.class));
        boolean exist = user.getCourses().stream()
                .anyMatch((c) -> c.getName().equals(courses1.getName()));
        assertTrue(exist);
    }

    @Test
    public void addStudent(){
        when(userRepo.findByUsername("abhi07")).thenReturn(Optional.empty());
        when(studentRepo.save(student)).thenReturn(student);
        StudentDTO savedStudent = service.addStudent(student);
        assertNotNull(savedStudent);
        assertEquals("abhi07",savedStudent.getUsername());
    }
}