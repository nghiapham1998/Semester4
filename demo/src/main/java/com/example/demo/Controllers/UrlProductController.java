package com.example.demo.Controllers;

import com.example.demo.Payload.Request.AddCategoryRequest;
import com.example.demo.Payload.Request.CreateOrderRequest;
import com.example.demo.Payload.Request.EditUrl;
import com.example.demo.domain.*;
import com.example.demo.repo.LinkTypeRepository;
import com.example.demo.repo.UrlProductRepository;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UrlProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/urlProduct")
public class UrlProductController {
    @Autowired
    UrlProductService productService;
    @Autowired
    LinkTypeRepository linkTypeRepository;
    @Autowired
    UrlProductRepository urlProductRepository;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/list/{username}")
    public ResponseEntity<?> ShowAllByUsername(@PathVariable String username) {
        return ResponseEntity.ok(productService.ShowAllByUser(username));
    }

    @GetMapping("/listLinkType")
    public ResponseEntity<?> ShowAllLinkType() {
        return ResponseEntity.ok((List<LinkType>)linkTypeRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UrlProduct url) throws IOException {
        String response = productService.addUrl(url);
        if(response != null){
            return ResponseEntity.ok(url);
        }else{
            return ResponseEntity.badRequest().body("Fail");
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody EditUrl urlEdit) throws IOException {
        UrlProduct urlProduct = urlProductRepository.findById(urlEdit.getId()).get();
        urlProduct.setName(urlEdit.getName());
        urlProduct.setLinkType(linkTypeRepository.findById(urlEdit.getLinkTypeId()).get());
        urlProduct.setUrl(urlEdit.getUrl());
        return ResponseEntity.ok(urlProductRepository.save(urlProduct));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        String response = productService.deleteUrl(id);
        if(response=="OK"){
            return new ResponseEntity("Success", HttpStatus.OK);
        }else{
            return new ResponseEntity("ID Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
