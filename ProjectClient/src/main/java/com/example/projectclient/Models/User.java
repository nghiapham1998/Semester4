package com.example.projectclient.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class User {

    private Long id;

    private String username;

    @NotBlank(message = "email is not blank")
    private String email;

    private String password;

    private String linkImage;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfbirth;

    private String province;

    private Boolean gender;

    @NotBlank(message = "phone is not blank")
    private String phone;

    @NotBlank(message = "address is not blank")
    private String address;

    private String nameImage;

    private Boolean enable;

    @NotBlank(message = "fullname is not blank")
    private String fullname;

    @NotBlank(message = "Lastname is not blank")
    private  String lastname;

    @NotBlank(message = "description is not blank")
    private String description;

    private Boolean locked;

    private Set<Role> roles;

    private String token;

    public User(Long id, String username, String email, Date dateOfbirth, String phone, String address,Set<Role> roles,String province) {
    }


}
