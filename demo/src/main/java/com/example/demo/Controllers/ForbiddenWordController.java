package com.example.demo.Controllers;

import com.example.demo.domain.ForbiddenReview;
import com.example.demo.repo.ForbiddenWordRepository;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forbiddenword")
public class ForbiddenWordController {

    @Autowired
    private ForbiddenWordRepository forbiddenWordRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(forbiddenWordRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addWord(@RequestBody ForbiddenReview forbiddenReview) {

        return ResponseEntity.ok(forbiddenWordRepository.save(forbiddenReview));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        forbiddenWordRepository.deleteById(id);

        return  ResponseEntity.ok("DELETE Successfully");
    }






}
