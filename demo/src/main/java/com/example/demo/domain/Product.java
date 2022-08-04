package com.example.demo.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String name;

    private String avatar;

    private String url;

    private String imageUrlcode;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private int status;
    private int year;
    private int count;

    private String token;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    private Date update_at;

    private Date delete_at;

    public Product(String description, String name, String avatar, String url, String imageUrlcode, User user, Date createdAt,int status, int year,String token) {
        this.description = description;
        this.name = name;
        this.avatar = avatar;
        this.url = url;
        this.imageUrlcode = imageUrlcode;
        this.user = user;
        this.createdAt = createdAt;
        this.status = status;
        this.year = year;
        this.token = token;
    }

    public Product(String description, String name, String avatar, String url, String imageUrlcode, User user, int status, int year, int count, Date createdAt) {
        this.description = description;
        this.name = name;
        this.avatar = avatar;
        this.url = url;
        this.imageUrlcode = imageUrlcode;
        this.user = user;
        this.status = status;
        this.year = year;
        this.count = count;
        this.createdAt = createdAt;
    }
}
