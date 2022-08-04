package com.example.demo.Payload.Request;


import lombok.*;


import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {


    private Long id;




    private Long category_id;


    private Long user_id;

    private String review;

    private Date create_at;
}
