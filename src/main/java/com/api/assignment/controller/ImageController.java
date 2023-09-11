package com.api.assignment.controller;

import com.api.assignment.config.ImgurConfig;
import com.api.assignment.entity.AuthRequest;
import com.api.assignment.service.ImageService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "v1/images", produces = "application/json")
public class ImageController {

    @Autowired
    ImgurConfig config;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ImageService imageService;

    @GetMapping("/auth")
    public RedirectView auth(){
        StringBuffer url = new StringBuffer()
                .append(config.getAuthorizeUrl())
                .append("?client_id=").append(config.getClientId())
                .append("&response_type=token");

        return new RedirectView(url.toString());
    }

    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request) {
        //Set imgur redirect as http://localhost:8080/imgur/redirect
        return "redirect.html";
    }

    @PostMapping("/saveToken")
    @ResponseBody
    public ResponseEntity saveToken(@RequestBody Map<String,String> data){
        config.setAccessToken(data.get("access_token"));
        config.setRefreshToken(data.get("refresh_token"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity upload(@RequestParam(name = "image") MultipartFile image) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = ((User)authentication.getPrincipal()).getUsername();
        String accessToken = config.getAccessToken();
        if(Strings.isBlank(accessToken))
            throw new RuntimeException("Access token not found, need admin auth");

        String base64Img = Base64.getEncoder().encodeToString(image.getBytes());
        return imageService.uploadImageByUser(userName, base64Img);

    }

    @PostMapping("/refreshToken")
    @ResponseBody
    public ResponseEntity refreshToken() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", config.getRefreshToken());
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getClientSecret());
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, header);
        ResponseEntity<Map> response = restTemplate.postForEntity(config.getAccessTokenUrl(), request, Map.class);

        LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
        String newAccessToken = (String) resBody.get("access_token");
        String newRefreshToken = (String) resBody.get("refresh_token");
        config.setAccessToken(newAccessToken);
        config.setRefreshToken(newRefreshToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view")
    @ResponseBody
    public ResponseEntity viewImages()  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = ((User)authentication.getPrincipal()).getUsername();

        return imageService.getImageByUser(userName);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity deleteImages(@RequestBody List<String> imageHash)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = ((User)authentication.getPrincipal()).getUsername();
        return imageService.deleteImageByUser(userName, imageHash);
    }


}
