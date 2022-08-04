package com.example.projectclient.Models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditProduct {
    private Long id;
    private String name;
    private String description;
    private MultipartFile avatar;

    public EditProduct(Long id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }




}
