package com.example.projectclient.Models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class changePasswordReset {

    private String password;


    private String email;
}
