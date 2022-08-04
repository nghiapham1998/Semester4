package com.example.demo.Payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Data
public class UpdateProfile {

    @NotBlank(message = "Lastname is not blank")
    @Size(min = 2,max = 50,message = "Lastname is min 2 character and max 50 character")
    private String lastname;


    @NotBlank(message = "Description is not blank")
    @Size(min = 3,max = 100,message = "description is min 3 character and max 100 character")
    private String description;

    @NotBlank(message = "Firstname is not blank")
    @Size(min = 2,max = 100,message = "Firstname is min 2 character and max 100 character")
    private String fullname;

    @NotBlank(message = "Phone is not blank")
    @Pattern(regexp="^[0-9]+$", message="the value must be positive integer")
    private String phone;

    @NotBlank(message = "address is not blank")
    private String address;

    @Email
    @NotBlank(message = "email is not blank")
    private String email;

    @Temporal(TemporalType.DATE)
    private Date dateOfbirth;


    private Boolean gender;
}
