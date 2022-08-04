package com.example.demo.repo;

import com.example.demo.domain.ProductAccessTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface ProductAccessTimeRepository extends CrudRepository<ProductAccessTime,Long> {

//    @Query(value = "select count(id) from ProductAccessTime  GROUP BY day(create_at)")
//    int countProductAccessTimeByCreate_atNow();
//
//    @Query(value = "select d from ProductAccessTime d where d.create_at between date_sub(now(),interval 1 WEEK) AND NOW()",nativeQuery = true)
//    List<ProductAccessTime> findProductAccessTimeByCreate_at();
//
    @Query(value = "SELECT   Day(create_at),count(id) ,year(create_at),month(create_at) from ProductAccessTime where month(create_at) = ?1 and product.id = ?2 group by day(create_at),year(create_at)")
    List<List<Integer>> countProductAccessTimeByCreate_atMonth(int month,Long id);

    @Query(value = "SELECT   count(id) ,year(create_at),month(create_at) from ProductAccessTime where  Year(create_at) = ?1 and product.id = ?2 group by month(create_at),year(create_at)")
    List<List<Integer>> countProductAccessTimeByCreate_atYear(int year ,Long id);
}
