package com.example.projectclient.Models;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditUrl {
    private Long id;

    private String name;

    private String url;

    private String username;

    private Long linkTypeId;
}
