package com.example.demo.model;

import com.example.demo.model.abstractModel.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "student")
public class Student extends User {

    @Id
    private String id;
    private List<Courses> courses;
    private  Role role = Role.STUDENT;

    public Student(String username, String password, String name) {
        super(username, password, name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(){
        this.role = Role.STUDENT;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", courses=" + courses +
                ", role=" + role +
                '}';
    }
}
