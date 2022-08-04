package com.example.demo.repo;

import com.example.demo.domain.Category;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {
    @Query("SELECT cate FROM Category cate WHERE cate.delete_at is null")
    List<Category> findAllCate();
}
