package com.example.demo.repo;

import com.example.demo.domain.ForbiddenReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForbiddenWordRepository extends JpaRepository<ForbiddenReview,Long> {

}
