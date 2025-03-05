package com.example.demo.model.modelDTO;

import com.example.demo.model.Courses;
import com.example.demo.model.Role;

public class TeacherDTO {
    private String name;
    private Role role;
    private Courses courses;
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses course) {
        this.courses = course;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
