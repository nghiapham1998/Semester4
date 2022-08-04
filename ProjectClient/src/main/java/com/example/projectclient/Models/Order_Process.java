package com.example.projectclient.Models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order_Process {
    private Long id;
    private String name;
    private String style;
    private String description;
}
