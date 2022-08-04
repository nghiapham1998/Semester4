package com.example.demo.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartProductAccess {

    private int year;

    private int day;

    private int count;

    private int month;
}
