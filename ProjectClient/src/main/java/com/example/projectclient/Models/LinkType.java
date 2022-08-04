package com.example.projectclient.Models;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkType {
    private Long id;

    private String name;

    private String placeholder;

    private String dataUrl;

    private String linkImage;

    private String href;
}
