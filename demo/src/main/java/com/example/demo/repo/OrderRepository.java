package com.example.demo.repo;


import com.example.demo.Payload.Request.Chart;
import com.example.demo.domain.Category;
import com.example.demo.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

    @Query("SELECT   month(createdAt), sum(price), year(createdAt) from  Orders group by MONTH(createdAt),YEAR(createdAt)  ")
    List<List<Integer>> getPriceByOrder();

    @Query("SELECT   month(createdAt), sum(price) , year(createdAt) from  Orders WHERE createdAt between ?1 and current_date and order_process = 3   group by year(createdAt),month(createdAt)  ")
    List<List<Integer>> getPriceByOrderS(Date date);

    @Query("SELECT   Day(createdAt), sum(price),month(createdAt),year(createdAt) from Orders where MONTH(createdAt) = ?1 and year(createdAt) = 2022 and order_process = 3 group by day(createdAt),year(createdAt) ")
    List<List<Integer>> getPriceMonthByOrder(int month);


    @Query("SELECT   month(createdAt), sum(price), year(createdAt) from  Orders where category.id=?1 and order_process = 3 group by MONTH(createdAt),year(createdAt) ORDER BY Year(createdAt)")
    List<List<Integer>> getPriceByOrderCategory(Long id);

    List<Orders> findByCategory(Category category);
}
