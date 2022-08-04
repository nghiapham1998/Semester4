package com.example.demo.repo;


import com.example.demo.domain.Product;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {
    @Query("SELECT product FROM Product product WHERE product.delete_at is null")
    List<Product> findAll();

    @Query("SELECT  product from  Product product where  product.user =:user")
    Product findProductByUser(User user);

    Optional<Product> findByToken(String Token);

    @Transactional
    @Modifying
    @Query("UPDATE Product c " +
            "SET c.status = 2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token);

    boolean existsByUser(User user);

    @Query("SELECT product from  Product product where  product.status = 1  ORDER BY product.count desc")
    List<Product> findTop10Product();


}
