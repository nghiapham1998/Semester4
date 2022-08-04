package com.example.demo.Payload.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChangeImageUserRequest {

    private String username;

    private MultipartFile image;
}
