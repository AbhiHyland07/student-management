package com.example.demo.model;

import com.example.demo.model.abstractModel.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teacher")
public class Teacher extends User {

    @Id
    private String id;
    private Role role = Role.TEACHER;
    private Courses courses;

    public Teacher(String username, String password, String name) {
        super(username, password, name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public void setRole(){
        this.role = Role.TEACHER;
    }
}
