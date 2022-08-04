package com.example.demo.Payload.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {
    private String name;
    private String price;
    private MultipartFile front;
    private MultipartFile back;
}
