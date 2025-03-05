package com.example.demo.service.serviceImpl;

import com.example.demo.configuration.JwtConfig.JwtUtil;
import com.example.demo.model.authentication.AuthenticationRequest;
import com.example.demo.model.authentication.AuthenticationResponse;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final JwtUtil jwtTokenUtil;
    private final UserAuthenticationService userAuthentication;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(JwtUtil jwtTokenUtil, UserAuthenticationService userAuthentication, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userAuthentication = userAuthentication;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest, HttpServletResponse response, HttpServletRequest request) {
        try {
            authenticate(authenticationRequest.getEmail(),authenticationRequest.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("User Credential does not match");
        }
        final UserDetails userDetails= userAuthentication.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt= jwtTokenUtil.generateToken((UserExtend) userDetails);
        return new AuthenticationResponse(jwt);
    }
    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new RuntimeException("User Disabled");
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Bad Credentials");
        }
    }
}
