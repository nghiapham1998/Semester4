package com.example.projectclient.Models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;


    private MultipartFile image;

    private String password;


    private String phone;

    private String address;


    private String fullname;

    private String province;

    private String dateOfbirth;


    private Boolean gender;


    private String description;

    private String lastname;
}
