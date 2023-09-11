package com.api.assignment.service.impl;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    UserRepository userRepository;



    @Override
    public UserResponse registerUser(User user) throws Exception {

            ObjectMapper mapper = new ObjectMapper();
            UserDetails userDetails = mapper.convertValue(user, UserDetails.class);

            userDetails.setPassword(Base64.getEncoder().encodeToString(userDetails.getPassword().getBytes()));

            if(!userRepository.findByemail(userDetails.getEmail()).isPresent())
                userRepository.save(userDetails);
            else
                throw new UserAlreadyExistException("User Already Exists with same EmailId");

        return new UserResponse(UserResponseStatusEnum.REQUEST_PROCESSED_SUCCESS.getMessageCode().toString(),
                UserResponseStatusEnum.REQUEST_PROCESSED_SUCCESS.getDescription(),
                UserResponseStatusEnum.REQUEST_PROCESSED_SUCCESS.getStatus(),
                new Date());
    }


}
