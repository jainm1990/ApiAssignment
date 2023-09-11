package com.api.assignment.service.impl;

import com.api.assignment.config.ImgurConfig;
import com.api.assignment.constants.UserResponseStatusEnum;
import com.api.assignment.entity.UserDetails;
import com.api.assignment.entity.UserImages;
import com.api.assignment.repository.UserRepository;
import com.api.assignment.response.UserResponse;
import com.api.assignment.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ImgurConfig config;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<UserResponse> uploadImageByUser(String userName, String base64Img) {

        UserResponse res = null;
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("Authorization", "Bearer " + config.getAccessToken());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", "your title");
        body.add("image", base64Img);
        body.add("description", "your desc");
        body.add("type", "base64");
        body.add("album", config.getAlbumId());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, header);
        ResponseEntity<Map> response = restTemplate.postForEntity(config.getUploadUrl(), request, Map.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
            LinkedHashMap<String, Object> resData = (LinkedHashMap<String, Object>) resBody.get("data");

            // add the image link in db for user
            saveUserImageLinkForUser(userName, (String)resData.get("link"), (String)resData.get("deletehash"));

            res = new UserResponse(UserResponseStatusEnum.IMAGE_UPLOAD_SUCCESS.getMessageCode().toString(),
                    UserResponseStatusEnum.IMAGE_UPLOAD_SUCCESS.getDescription(),
                    UserResponseStatusEnum.IMAGE_UPLOAD_SUCCESS.getStatus(),
                    new Date());
        } else {
            res = new UserResponse(UserResponseStatusEnum.ERROR_UPLOADING_IMAGE.getMessageCode().toString(),
                    UserResponseStatusEnum.ERROR_UPLOADING_IMAGE.getDescription(),
                    UserResponseStatusEnum.ERROR_UPLOADING_IMAGE.getStatus(),
                    new Date());
        }

        return ResponseEntity.status(response.getStatusCode()).body(res);
    }

    @Override
    public ResponseEntity<UserResponse> getImageByUser(String userName) {
        UserDetails userDetails = userRepository.findByemail(userName).get();


        UserResponse res = new UserResponse(UserResponseStatusEnum.IMAGE_VIEW_SUCCESS.getMessageCode().toString(),
                userDetails.getImages().stream().map(i -> i.getImageLink() + "," + i.getHash()).collect(Collectors.toList()).toString(),
                UserResponseStatusEnum.IMAGE_VIEW_SUCCESS.getStatus(),
                new Date());

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<UserResponse> deleteImageByUser(String userName, List<String> imageHash) {

        UserResponse res = null;

        try {
            imageHash.stream().forEach(eachImageHash -> {
                restTemplate.delete(config.getDeleteurl() + "/" + eachImageHash);
            });

            res = new UserResponse(UserResponseStatusEnum.DELETE_IMAGE_SUCCESS.getMessageCode().toString(),
                    UserResponseStatusEnum.DELETE_IMAGE_SUCCESS.getDescription(),
                    UserResponseStatusEnum.DELETE_IMAGE_SUCCESS.getStatus(),
                    new Date());

            // Delete from DB
            imageHash.stream().forEach(eachImageHash -> {
                deleteUserImageLinkForUser(userName, eachImageHash);
            });
        } catch (Exception ex) {
            res = new UserResponse(UserResponseStatusEnum.DELETE_IMAGE_FAILED.getMessageCode().toString(),
                    UserResponseStatusEnum.DELETE_IMAGE_FAILED.getDescription(),
                    UserResponseStatusEnum.DELETE_IMAGE_FAILED.getStatus(),
                    new Date());
        }

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    private void saveUserImageLinkForUser(String userName, String imageLink, String hash) {
        UserDetails userDetails = userRepository.findByemail(userName).get();
        List<UserImages> userImagesList = userDetails.getImages();
        UserImages userImages = new UserImages();
        userImages.setImageLink(imageLink);
        userImages.setImageid(456);
        userImages.setHash(hash);
        userImagesList.add(userImages);

        userDetails.setImages(userImagesList);
        userRepository.save(userDetails);
    }

    private void deleteUserImageLinkForUser(String userName, String imageHash) {
        UserDetails userDetails = userRepository.findByemail(userName).get();
        List<UserImages> userImagesList = userDetails.getImages();

        UserImages userImagesForDel = userImagesList.stream().
                filter(userImages -> userImages.getHash().equals(imageHash)).
                findFirst().get();

        //userImagesList.removeIf(userImages -> userImages.getHash().equals(imageHash));
        userImagesList.remove(userImagesForDel);

        userDetails.setImages(userImagesList);
        userRepository.save(userDetails);
    }
}
