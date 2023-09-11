package com.api.assignment.dto;

import com.api.assignment.entity.UserImages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String password;

    //private String matchingPassword;

    @NotBlank(message = "Email may not be Blank")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid Email")
    private String email;

    @Pattern(regexp="([0-9]+)", message = "Invalid Mobile Number")
    @Size(min = 10, max = 12, message = "Mobile Number MIN 10 digit and MAX 12 digit allowed")
    private String mobileNumber;

    //private List<UserImages> images;
}
