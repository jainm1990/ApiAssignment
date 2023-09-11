package com.api.assignment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String responseCode; // 01, 02, 03
    private String responseMessage; //
    private String status; // Ok, Failed
    private Date timeStamp;
}
