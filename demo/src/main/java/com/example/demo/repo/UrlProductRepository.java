package com.example.demo.repo;


import com.example.demo.domain.UrlProduct;
import com.example.demo.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlProductRepository extends CrudRepository<UrlProduct,Long> {
    public List<UrlProduct> findUrlProductByUser(User user);
}
