package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.domain.UrlProduct;
import com.example.demo.domain.User;
import com.example.demo.repo.UrlProductRepository;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlProductService {
    @Autowired
    UrlProductRepository urlProductRepository;
    @Autowired
    UserRepo userRepo;

    public List<UrlProduct> ShowAllByUser(String username) {
        User user = userRepo.findByUsername(username).get();
        return (List<UrlProduct>) urlProductRepository.findUrlProductByUser(user);
    }

    public String addUrl(UrlProduct url){
        urlProductRepository.save(url);
        return "OK";
    }

    public String deleteUrl(Long id){
        if(urlProductRepository.existsById(id)){
            urlProductRepository.deleteById(id);
            return "OK";
        }
        return null;
    }
}
