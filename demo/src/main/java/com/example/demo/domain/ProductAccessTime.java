package com.example.demo.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class ProductAccessTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public ProductAccessTime(Product product, Date create_at) {
        this.product = product;
        this.create_at = create_at;
    }

    private Date create_at;

}
