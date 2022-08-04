package com.example.projectclient.Models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlProduct {
    private Long id;

    private String name;

    private String url;

    private Product product;

    private User user;

    private LinkType linkType;
}
