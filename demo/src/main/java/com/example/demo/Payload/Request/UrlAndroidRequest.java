package com.example.demo.Payload.Request;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor//constructor with all parameter
@NoArgsConstructor
public class UrlAndroidRequest {
    //    json for postman test
//    "name": "Twitter",
//    "url": "twitter.com/nero3103",
//    "type_id" : 1,
//    "product_id" : 7,
//    "user_id" : 9
    private String name;

    private String url;

    private Long type_id;

    private Long product_id;

    private Long user_id;

}
