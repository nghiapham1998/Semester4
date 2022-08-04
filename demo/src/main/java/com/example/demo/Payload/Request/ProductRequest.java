package com.example.demo.Payload.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long id ;

    private LocalDateTime create_at;

    private LocalDateTime delete_at;

    private LocalDateTime update_at;

    private String description;

    private MultipartFile qrcode;

    private MultipartFile url;

    private Long user_id;

}
