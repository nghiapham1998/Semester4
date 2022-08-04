package com.example.demo.Controllers;


import com.example.demo.Exception.ApiRequestException;
import com.example.demo.Payload.Request.ReviewRequest;
import com.example.demo.domain.ForbiddenReview;
import com.example.demo.domain.Review;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.ForbiddenWordRepository;
import com.example.demo.repo.ReviewRepository;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ForbiddenWordRepository forbiddenWordRepository;
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest review) {
        var forbiden = forbiddenWordRepository.findAll();
        for (ForbiddenReview c : forbiden){
            if (review.getReview().contains(c.getWord())){
                throw new ApiRequestException("Your comment have forbidden word !!");
            }
        }
        var user = userRepo.findById(review.getUser_id()).get();
        var category = categoryRepository.findById(review.getCategory_id()).get();
        Review r = new Review();
        r.setUser(user);
        r.setCategory(category);
        r.setReview(review.getReview());
        r.setCreate_at(new Date());
        reviewRepository.save(r);
        return ResponseEntity.ok("asdasdas");
    }


    @GetMapping("/showAll")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(reviewRepository.findAll());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getDetails(@PathVariable Long id) {

        return ResponseEntity.ok(reviewRepository.findById(id).get());
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reviewRepository.deleteById(id);

        return  ResponseEntity.ok("DELETE Successfully");
    }


    @GetMapping("/showAll/{categoryId}")
    public ResponseEntity<?> getAllById(@PathVariable Long categoryId){

        return ResponseEntity.ok(reviewRepository.findAllReview(categoryId));
    }
}
