package com.example.demo.service.serviceImpl;

import com.example.demo.exception.model.ResourceAlreadyPresent;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.model.Courses;
import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import com.example.demo.model.UserName;
import com.example.demo.model.modelDTO.StudentDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository repository;
    private final ModelMapper mapper = new ModelMapper();
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository repository,
                              PasswordEncoder encoder, UserRepository userRepository,
                              TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.repository = repository;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public StudentDTO addStudent(Student student) {
        Optional<UserName> userNameOptional = userRepository.findByUsername(student.getUsername());
        if (userNameOptional.isPresent()){
            throw new ResourceAlreadyPresent("The username "+student.getUsername()+" is already present");
        }
        student.setPassword(encoder.encode(student.getPassword()));
        UserName userName = new UserName();
        userName.setUsername(student.getUsername());
        userRepository.save(userName);
        studentRepository.save(student);
        return mapper.map(student,StudentDTO.class);
    }

    @Override
    public StudentDTO updateCourse(String username, String courseID) {
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isEmpty()){
            throw new ResourceNotFound("No student with username "+username+" is present");
        }

        Optional<Courses> courses = repository.findByCourseId(courseID);
        if (courses.isEmpty()){
            throw new ResourceNotFound("No course with courseId "+courseID+" is present");
        }
        List<Courses> coursesList = student.get().getCourses();
        if (coursesList == null){
            coursesList = new ArrayList<>();
        }
        if ( coursesList.stream().noneMatch((c)-> c.getCourseId().equals(courseID)) ){
            coursesList.add(courses.get());
            student.get().setCourses(coursesList);
            studentRepository.save(student.get());
        }
        return mapper.map(student.get(),StudentDTO.class);
    }

    @Override
    public StudentDTO getStudent(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Student studentDetails = studentRepository.findByUsername(username)
                                                  .orElseThrow(()->new ResourceNotFound("No student with username "+username+" is present"));
        Optional<Teacher> teacherOptional = teacherRepository.findByUsername(name);
        if (teacherOptional.isPresent()){
            Teacher teacher = teacherOptional.get();
            if (studentDetails.getCourses().stream().
                    noneMatch((c)->c.getCourseId().equals(teacher.getCourses().getCourseId()))){
                throw new AccessDeniedException("Access is Denied");
            }
        }

        return mapper.map(studentDetails,StudentDTO.class);
    }
}
