package com.api.assignment.controller;

import com.api.assignment.constants.UserResponseStatusEnum;
import com.api.assignment.dto.User;
import com.api.assignment.entity.AuthRequest;
import com.api.assignment.entity.UserDetails;
import com.api.assignment.exception.UserAlreadyExistException;
import com.api.assignment.repository.UserRepository;
import com.api.assignment.response.UserResponse;
import com.api.assignment.service.UserManagementService;
import com.api.assignment.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Base64;

@RestController
@RequestMapping(value = "v1/user", produces = "application/json")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserManagementService userManagementService;

    @PostMapping("/registration")
    ResponseEntity<UserResponse> showRegistrationForm(@Valid @RequestBody User userRequest) throws Exception{

        try {

            UserResponse userResponse = userManagementService.registerUser(userRequest);

            return new ResponseEntity(userResponse, HttpStatus.OK);

        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(),
                            Base64.getEncoder().encodeToString(authRequest.getPassword().getBytes()))
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
}
