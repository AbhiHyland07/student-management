package com.example.demo.service;

import com.example.demo.model.authentication.AuthenticationRequest;
import com.example.demo.model.authentication.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest, HttpServletResponse response, HttpServletRequest request);

}
