package com.example.demo.servicetest;

import com.example.demo.model.Courses;
import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import com.example.demo.model.modelDTO.CourseDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.serviceImpl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private CourseRepository courseRepo;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Courses courses;
    private Teacher teacher;
    private Student student;
    private Courses updatedCourses;

    @BeforeEach
    public void setUp(){
        courses = new Courses();
        courses.setName("Physics");
        courses.setCourseId("1");
        courses.setDescription("Physics");
        updatedCourses = new Courses();
        updatedCourses.setName("Chemistry");
        updatedCourses.setCourseId("1");
        updatedCourses.setDescription("Chemistry");
        courseService = new CourseServiceImpl(courseRepo,studentRepo,teacherRepository);
        teacher = new Teacher("abhi7","Hello","Abhi");
        teacher.setCourses(courses);
        student = new Student("abhi8","Hello","Abhi");
        List<Courses> coursesList = new ArrayList<>();
        coursesList.add(courses);
        student.setCourses(coursesList);
    }

    @Test
    public void addCourse(){
        when(courseRepo.findByCourseId(courses.getCourseId())).thenReturn(Optional.empty());
        when(courseRepo.save(courses)).thenReturn(courses);
        CourseDTO courseDTO = courseService.addCourses(courses);
        assertNotNull(courseDTO);
        assertEquals("Physics",courseDTO.getDescription());
        assertEquals("Physics",courseDTO.getName());
    }

    @Test
    public void deleteCourse(){
        when(courseRepo.findByCourseId(courses.getCourseId())).thenReturn(Optional.of(courses));
        when(studentRepo.findAll()).thenReturn(Collections.singletonList(student));
        when(teacherRepository.findAll()).thenReturn(Collections.singletonList(teacher));
        Optional<Courses> courses1 = courseRepo.findByCourseId(courses.getCourseId());
        assertEquals("Physics",courses1.get().getDescription());
        assertEquals("Physics",courses1.get().getName());
        willDoNothing().given(courseRepo).delete(courses);
        courseService.deleteCourse(courses.getCourseId());
        verify(courseRepo, times(1)).delete(courses);
        verify(studentRepo,times(1)).save(student);
        verify(teacherRepository,times(1)).save(teacher);
        assertNull(teacher.getCourses());
        assertTrue(student.getCourses().isEmpty());
    }

    @Test
    public void getCourse(){
        when(courseRepo.findByCourseId(courses.getCourseId())).thenReturn(Optional.of(courses));
        CourseDTO courseDTO = courseService.getCourse(courses.getCourseId());
        assertEquals("Physics",courseDTO.getName());
        assertEquals("Physics",courseDTO.getDescription());
    }

    @Test
    public void updateCourse(){
        when(courseRepo.findByCourseId(courses.getCourseId())).thenReturn(Optional.of(courses))
                .thenReturn(Optional.of(updatedCourses));

        when(studentRepo.findAll()).thenReturn(Collections.singletonList(student));
        when(teacherRepository.findAll()).thenReturn(Collections.singletonList(teacher));
        when(courseRepo.save(updatedCourses)).thenReturn(updatedCourses);
        Optional<Courses> courses1 = courseRepo.findByCourseId(courses.getCourseId());
        assertEquals("Physics",courses1.get().getDescription());
        assertEquals("Physics",courses1.get().getName());
        courseService.updateCourse(courses.getCourseId(),updatedCourses);
        verify(studentRepo,times(1)).save(student);
        verify(teacherRepository,times(1)).save(teacher);
        verify(courseRepo, times(1)).save(updatedCourses);
        assertEquals("Chemistry",teacher.getCourses().getName());
        assertEquals("Chemistry",teacher.getCourses().getDescription());
        assertEquals("Chemistry",student.getCourses().getFirst().getName());
        assertEquals("Chemistry",student.getCourses().getFirst().getDescription());
    }
}
