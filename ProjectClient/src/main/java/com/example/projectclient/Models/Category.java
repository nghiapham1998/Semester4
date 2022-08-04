package com.example.projectclient.Models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    private Long id;
    private int price;
    private String name;
    private String frontImage;
    private String backImage;
    private int quantity;
    private Date create_at;
    private Date update_at;
    private Date delete_at;

    public Category(int price, String name) {
        this.price = price;
        this.name = name;
    }
}