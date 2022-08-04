package com.example.demo.repo;


import com.example.demo.domain.Discount;
import org.springframework.data.repository.CrudRepository;

public interface DiscountRepository extends CrudRepository<Discount,Long> {
}
