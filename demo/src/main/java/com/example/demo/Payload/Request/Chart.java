package com.example.demo.Payload.Request;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chart {

    private int price;

    private int month;

    private int day;
    private int year;


}
