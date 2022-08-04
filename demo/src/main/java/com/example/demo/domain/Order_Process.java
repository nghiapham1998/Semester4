package com.example.demo.domain;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order_Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String style;
    private String description;

    public Order_Process(String name, String style, String description) {
        this.name = name;
        this.style= style;
        this.description= description;
    }
}
