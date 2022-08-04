package com.example.demo.Controllers;

import com.example.demo.Payload.Request.AddCategoryRequest;
import com.example.demo.Payload.Request.ChangeImageUserRequest;
import com.example.demo.domain.Category;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.ProductRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CloudBinary.CloudBinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryRepository cateRepo;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(cateRepo.findAllCate());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getDetails(@PathVariable Long id) {
        if(cateRepo.existsById(id)){
            return ResponseEntity.ok(cateRepo.findById(id).get());
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category not found");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category) throws IOException {
        String response = categoryService.addNew(category);
        if (response == "OK") {
            return ResponseEntity.ok("Create successfully!");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Create failed!");
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestBody Category category) {
        if (cateRepo.existsById(category.getId())) {
            Category categoryUpdate = cateRepo.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.setPrice(category.getPrice());
            categoryUpdate.setQuantity(category.getQuantity());
            categoryUpdate.setFrontImage(category.getFrontImage());
            categoryUpdate.setBackImage(category.getBackImage());
            categoryUpdate.setUpdate_at(new Date());
            cateRepo.save(categoryUpdate);
            return ResponseEntity.ok(categoryUpdate);
        } else {
            return ResponseEntity.badRequest().body("Category is not exist...");
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        Category category = cateRepo.findById(id).get();
        if(orderRepository.findByCategory(cateRepo.findById(id).get()).size()>0){
            return  new ResponseEntity("Cannot delete!", HttpStatus.BAD_REQUEST);
        }

        if (category.getDelete_at() == null) {
            category.setDelete_at(new Date());
        } else {
            category.setDelete_at(null);
        }
        cateRepo.save(category);
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    @GetMapping("/addQuantity/{id}/{number}")
    public ResponseEntity<?> addQuantity(@PathVariable Long id, @PathVariable int number) {
        Category category = cateRepo.findById(id).get();
        if (category != null){
            int quantity = category.getQuantity();
            quantity = quantity + number;
            category.setQuantity(quantity);
            cateRepo.save(category);
            return new ResponseEntity("Success", HttpStatus.OK);
        }
        return  new ResponseEntity("Add Quantity Fail", HttpStatus.BAD_REQUEST);
    }
}
