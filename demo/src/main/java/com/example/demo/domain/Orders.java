package com.example.demo.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;

    @ManyToOne(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    private String address;

    private String phone;

    private String fullname;

    @ManyToOne
    @JoinColumn(name="process_id")
    private Order_Process order_process;

    @ManyToOne(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
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
