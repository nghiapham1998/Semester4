package com.example.projectclient.Models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCategory {
    private Long id;
    private int price;
    private String name;
    private int quantity;
    private MultipartFile frontImage;
    private MultipartFile backImage;
}
