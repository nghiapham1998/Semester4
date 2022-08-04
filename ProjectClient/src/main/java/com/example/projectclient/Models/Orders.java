package com.example.projectclient.Models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    private Long id;

    private int price;

    private Category category;

    private User user;

    private String address;

    private String phone;

    private String fullname;

    private Order_Process order_process;

    private Product product;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date confirmedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finishedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date canceledAt;
}
