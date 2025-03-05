package com.example.demo.model.authentication;

import org.springframework.security.core.userdetails.User;

public class UserExtend extends User {
    private final String id;
    private final User user;
    public UserExtend(User user, String id) {
        super(user.getUsername(), user.getPassword(),user.getAuthorities());
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // Return the username from the User object
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Return the password from the User object
    }
}
