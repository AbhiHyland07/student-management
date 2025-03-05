package com.example.demo.service.serviceImpl;

import com.example.demo.exception.model.ResourceAlreadyPresent;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.model.Courses;
import com.example.demo.model.Teacher;
import com.example.demo.model.UserName;
import com.example.demo.model.modelDTO.TeacherDTO;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository repository;
    private final ModelMapper mapper = new ModelMapper();
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, CourseRepository repository,
                              PasswordEncoder encoder, UserRepository userRepository){
        this.teacherRepository = teacherRepository;
        this.repository = repository;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }
    @Override
    public TeacherDTO addTeacher(Teacher teacher, String id) {
        Optional<UserName> userNameOptional = userRepository.findByUsername(teacher.getUsername());
        if (userNameOptional.isPresent()){
            throw new ResourceAlreadyPresent("The username "+userNameOptional.get().getUsername()+" is present");
        }

        Optional<Courses> courses = repository.findByCourseId(id);
        if (courses.isEmpty()){
            throw new ResourceNotFound("No course with courseId "+id+" is present");
        }
        teacher.setCourses(courses.get());
        teacher.setPassword(encoder.encode(teacher.getPassword()));
        UserName userName = new UserName();
        userName.setUsername(teacher.getUsername());
        userRepository.save(userName);
        teacherRepository.save(teacher);
        return mapper.map(teacher,TeacherDTO.class);
    }
}
