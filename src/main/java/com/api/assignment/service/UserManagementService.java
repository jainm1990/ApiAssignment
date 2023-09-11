package com.api.assignment.service;

import com.api.assignment.dto.User;
import com.api.assignment.response.UserResponse;

public interface UserManagementService {

    public UserResponse registerUser(User user) throws Exception;
}
