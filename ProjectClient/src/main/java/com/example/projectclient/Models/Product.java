package com.example.projectclient.Models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;

    private String description;

    private String name;
    private String avatar;

    private String url;

    private String imageUrlcode;

    private User user;

    private int status;

    private int year;
    private int count;

    private String token;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    private Date update_at;

    private Date delete_at;

    public Product(Long id, String description, String name, String avatar) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.avatar = avatar;
    }

    public Product(String description, String name, String url, String imageUrlcode, User user, Date createdAt, int status, String token) {
        this.description = description;
        this.name = name;
        this.url = url;
        this.imageUrlcode = imageUrlcode;
        this.user = user;
        this.createdAt = createdAt;
        this.status = status;
        this.token = token;
    }
}
