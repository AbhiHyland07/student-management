package com.example.demo.service.serviceImpl;

import com.example.demo.exception.model.ResourceAlreadyPresent;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.model.Courses;
import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import com.example.demo.model.modelDTO.CourseDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, StudentRepository studentRepository,
                             TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public CourseDTO addCourses(Courses courses) {
        String id = courses.getCourseId();
        Optional<Courses> courses1 = courseRepository.findByCourseId(id);
        if (courses1.isPresent()){
            throw new ResourceAlreadyPresent("Course with courseId "+id+" is present");
        }

        courseRepository.save(courses);
        return mapper.map(courses, CourseDTO.class);
    }

    @Override
    public CourseDTO deleteCourse(String id) {
        Optional<Courses> courses1 = courseRepository.findByCourseId(id);
        if (courses1.isEmpty()){
            throw new ResourceNotFound("No course with courseId "+id+" is present");
        }
        courseRepository.delete(courses1.get());
        for (Student student : studentRepository.findAll()) {
            student.getCourses().removeIf(course -> course.getCourseId().equals(id));
            studentRepository.save(student); // Save the updated student document
        }

        for (Teacher teacher : teacherRepository.findAll()) {
            if (teacher.getCourses() != null && teacher.getCourses().getCourseId().equals(id)) {
                teacher.setCourses(null);  // Set the course to null
                teacherRepository.save(teacher);  // Save the updated teacher document
            }
        }

        return mapper.map(courses1.get(), CourseDTO.class);
    }

    @Override
    public CourseDTO getCourse(String id) {
        Courses courses = courseRepository.findByCourseId(id)
                                          .orElseThrow(()->  new ResourceNotFound("No course with courseId "+id+" is present"));
        return mapper.map(courses,CourseDTO.class);
    }

    @Override
    public CourseDTO updateCourse(String id, Courses courses) {
        Optional<Courses> courses1 = courseRepository.findByCourseId(id);
        if (courses1.isEmpty()){
            throw new ResourceNotFound("No course with courseId "+id+" is present");
        }
        courseRepository.delete(courses1.get());
        courses.setCourseId(id);
        courseRepository.save(courses);
        Courses courses2 = courseRepository.findByCourseId(id).get();
        for (Student student : studentRepository.findAll()) {
            if (student.getCourses() == null){
                continue;
            }
            for (Courses studentCourse : student.getCourses()) {
                if (studentCourse.getCourseId().equals(id)) {
                    student.getCourses().remove(studentCourse); // or other updates
                    student.getCourses().add(courses2);
                    studentRepository.save(student); // Save the updated student
                    break;
                }
            }
        }

        for (Teacher teacher : teacherRepository.findAll()) {
            if (teacher.getCourses() != null && teacher.getCourses().getCourseId().equals(id)) {
                teacher.setCourses(courses2);  // Set the course to null
                teacherRepository.save(teacher);  // Save the updated teacher document
            }
        }

        return mapper.map(courses2, CourseDTO.class);
       }
    }