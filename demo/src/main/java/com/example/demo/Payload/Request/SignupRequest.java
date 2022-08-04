package com.example.demo.Payload.Request;

import com.example.demo.domain.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20,message = "Username size min 3 or maximum 20 character")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;


    private MultipartFile image;
    @NotBlank
    @Size(min = 6, max = 40, message = "Password size must be between 6 and 40")
    private String password;

    @NotBlank(message = "Phone is not blank")
    @Pattern(regexp="\\d{10}",message="Phone number wrong format !!!")
    private String phone;
    @NotBlank(message = "Address is not blank")
    private String address;

    @NotBlank(message = "Firstname is not blank")
    private String fullname;


    @Temporal(TemporalType.DATE)
    private Date  dateOfbirth;


    private Boolean gender;


    private String description;
    @NotBlank(message = "Lastname is not blank")
    private String lastname;

    private String province;
    public SignupRequest(String username, String email, String password, Set<String> roles, String phone, String address, String fullname,
                         String lastname, String description, Date dateOfbirth, Boolean gender,String province) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.fullname = fullname;
        this.lastname = lastname;
        this.description = description;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        this.province = province;
    }
}
