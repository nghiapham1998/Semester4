package com.example.demo.repo;

import com.example.demo.domain.Orders;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AndroidOrderRepo extends JpaRepository<Orders,Long> {
    //for android
    //get Orders List by user
    List<Orders> findOrdersByUser(User user);

    //tính doanh thu(SNgoc)
    @Query(value = "select price from Orders where order_process.id = 3")
    List<Double> revenueOrder();

    //count order status(SNgoc)
    //waiting
    @Query(value = "select count(id) from Orders where order_process.id = 1")
    int orderWaiting();
    //delivery
    @Query(value = "select count(id) from Orders where order_process.id = 2")
    int orderDelivery();
    //success
    @Query(value = "select count(id) from Orders where order_process.id = 3")
    int orderSuccess();
    //cancel
    @Query(value = "select count(id) from Orders where order_process.id = 4")
    int orderCancel();
}
