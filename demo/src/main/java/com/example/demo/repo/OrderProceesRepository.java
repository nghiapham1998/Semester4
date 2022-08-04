package com.example.demo.repo;


import com.example.demo.domain.Order_Process;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProceesRepository extends CrudRepository<Order_Process,Long> {

}
