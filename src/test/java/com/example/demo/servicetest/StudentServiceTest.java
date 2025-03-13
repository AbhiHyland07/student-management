package com.example.demo.servicetest;


import com.example.demo.model.Courses;
import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.model.modelDTO.CourseDTO;
import com.example.demo.model.modelDTO.StudentDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private CourseRepository courseRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private TeacherRepository teacherRepo;

    @InjectMocks
    private StudentServiceImpl service;

    @InjectMocks
    private CourseServiceImpl courseService;


    private Student student;
    private Courses courses;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final ModelMapper mapper = new ModelMapper();
    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    public void setUp(){
        student = new Student("abhi07","Hello","Abhigyan");
        student.setPassword(encoder.encode("Hello"));
        courses = new Courses();
        courses.setCourseId("2");
        courses.setName("Course");
        courses.setDescription("Course2");
        service = new StudentServiceImpl(studentRepo,courseRepo,encoder,userRepo,teacherRepo);
        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(Role.STUDENT.name()));
    }
    @Test
    public void testGetUserByUsername() {
        List<Courses> coursesList = new ArrayList<>();
        coursesList.add(courses);
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("abhi07");

        // Step 2: Mock the SecurityContext object
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Step 3: Set the mocked SecurityContext into SecurityContextHolder
        SecurityContextHolder.setContext(mockSecurityContext);
        student.setCourses(coursesList);
        when(studentRepo.findByUsername("abhi07")).thenReturn(Optional.ofNullable(student));
        StudentDTO user = service.getStudent("abhi07");
        assertNotNull(user);
        assertEquals("abhi07",user.getUsername());
    }

    @Test
    public void addCourse() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("abhi07");

        // Step 2: Mock the SecurityContext object
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Step 3: Set the mocked SecurityContext into SecurityContextHolder
        SecurityContextHolder.setContext(mockSecurityContext);
        when(studentRepo.findByUsername("abhi07")).thenReturn(Optional.ofNullable(student));
        when(teacherRepo.findByUsername("abhi07")).thenReturn(Optional.empty());
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