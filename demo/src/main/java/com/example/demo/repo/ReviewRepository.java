package com.example.demo.repo;


import com.example.demo.domain.Category;
import com.example.demo.domain.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {

    @Query("SELECT review FROM Review review WHERE review.category.id =?1")
    List<Review> findAllReview(Long id);
}
