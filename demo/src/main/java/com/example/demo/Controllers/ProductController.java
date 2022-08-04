package com.example.demo.Controllers;

import com.example.demo.domain.Product;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepository productRepository;
    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productService.ShowAll());
    }

    @GetMapping("/getProduct/{username}")
    public ResponseEntity<?> getProduct(@PathVariable String username) {
        var user = userRepo.findByUsername(username);
        Product product = productRepository.findProductByUser(user.get());
        if(product.getStatus() == 1){
            return ResponseEntity.ok(product);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestBody Product productUpdate) {
        if (productRepository.existsById(productUpdate.getId())) {
            productRepository.save(productUpdate);
            return ResponseEntity.ok(productUpdate);
        } else {
            return ResponseEntity.badRequest().body("Product is not exist...");
        }
    }

    @GetMapping("/changeStatus/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        if(productRepository.existsById(id)){
            Product product = productRepository.findById(id).get();
            if(product.getStatus() == 0){
                product.setStatus(1);
            }else{
                product.setStatus(0);
            }
            productRepository.save(product);
            return ResponseEntity.ok(product);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
        }
    }

    @GetMapping("/topProductCount")
    public ResponseEntity<?> topProductCount() {
        var value = productRepository.findTop10Product().stream().limit(3);
        return ResponseEntity.ok(value);
    }
}
