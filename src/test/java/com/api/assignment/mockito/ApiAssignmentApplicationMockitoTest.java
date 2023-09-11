package com.api.assignment.mockito;

import com.api.assignment.dto.User;
import com.api.assignment.entity.AuthRequest;
import com.api.assignment.repository.UserRepository;
import com.api.assignment.response.UserResponse;
import com.api.assignment.service.UserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiAssignmentApplicationMockitoTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    ObjectMapper om = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void addUserTest() throws Exception {
        User user = new User();
        user.setEmail("mamta.mehta@wibmo.com");
        user.setPassword("QWERTY");
        user.setFirstName("Mamta");
        user.setLastName("Mehta");
        user.setMobileNumber("88990000011");

        String jsonRequest = om.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/v1/user/registration").content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        UserResponse response = om.readValue(resultContent, UserResponse.class);
        Assert.assertTrue(response.getStatus() == HttpStatus.OK.toString());

    }

    @Test
    public void loginTest() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("QWERTY");
        authRequest.setUserName("mamta.mehta@wibmo.com");
        String jsonRequest = om.writeValueAsString(authRequest);
        MvcResult result = mockMvc
                .perform(post("/v1/user/authenticate").content(jsonRequest).
                        contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        UserResponse response = om.readValue(resultContent, UserResponse.class);
        Assert.assertTrue(response.getStatus() == HttpStatus.OK.toString());

    }

}