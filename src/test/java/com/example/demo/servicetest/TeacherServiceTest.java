package com.example.demo.servicetest;

import com.example.demo.model.Courses;
import com.example.demo.model.Teacher;
import com.example.demo.model.modelDTO.TeacherDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.serviceImpl.CourseServiceImpl;
import com.example.demo.service.serviceImpl.TeacherServiceImpl;
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
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CourseRepository courseRepo;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Teacher teacher;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private Courses courses;

    @BeforeEach
    public void setUp(){
        teacher = new Teacher("abhi07","Hello","Abhigyan");
        teacher.setPassword(encoder.encode("Hello"));
        courses = new Courses();
        courses.setCourseId("2");
        courses.setName("Course");
        courses.setDescription("Course2");
        teacherService = new TeacherServiceImpl(teacherRepository,courseRepo,encoder,userRepo);
    }

    @Test
    public void addStudent(){
        when(userRepo.findByUsername("abhi07")).thenReturn(Optional.empty());
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        when(courseRepo.findByCourseId("2")).thenReturn(Optional.ofNullable(courses));
        TeacherDTO savedTeacher = teacherService.addTeacher(teacher,"2");
        assertNotNull(savedTeacher);
        assertEquals("abhi07", savedTeacher.getUsername());
        assertEquals("2",savedTeacher.getCourses().getCourseId());
        assertEquals("2",savedTeacher.getCourses().getCourseId());
    }
}
