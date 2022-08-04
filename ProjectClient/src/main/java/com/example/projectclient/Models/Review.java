package com.example.projectclient.Models;

import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long id;




    private Category category;


    private User user;

    private String review;

    private Date create_at;
}
