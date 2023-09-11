package com.api.assignment.service;

import com.api.assignment.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ImageService {

    public ResponseEntity<UserResponse> uploadImageByUser(String userName, String image);

    public ResponseEntity<UserResponse> getImageByUser(String userName);

    public ResponseEntity<UserResponse> deleteImageByUser(String userName, List<String> imageHash);
}
