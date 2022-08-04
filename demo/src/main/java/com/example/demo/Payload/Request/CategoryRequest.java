package com.example.demo.Payload.Request;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private Long id;

    private int price;

    private String name;

    private String url;

    private String secretSeri;

    private String frontImage;


    private String backImage;

    private Date create_at;

    private Date update_at;

    private Date delete_at;
}
