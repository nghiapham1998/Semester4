package com.example.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Setter
@Getter

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;

    @Column(nullable=false)
    @NotEmpty
    private String name;
    @Column
    private String frontImage;
    @Column
    private String backImage;
    @Column
    private int quantity;
    @Column
    private Date create_at;
    @Column
    private Date update_at;
    @Column
    private Date delete_at;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products;
//
//    @OneToMany(mappedBy = "category")
//    private List<Orders> orders;


    public Category(int price, String name, int quantity, String frontImage, String backImage, Date create_at, Date update_at, Date delete_at) {
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.create_at = create_at;
        this.update_at = update_at;
        this.delete_at = delete_at;
    }

    public Category() {
    }
}
