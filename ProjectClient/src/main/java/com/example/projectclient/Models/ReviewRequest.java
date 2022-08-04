package com.example.projectclient.Models;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long category_id;


    private Long user_id;

    private String review;
}
