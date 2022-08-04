package com.example.demo.Controllers;

import com.example.demo.Exception.ApiRequestException;
import com.example.demo.Payload.Request.UpdateProfile;
import com.example.demo.Payload.Request.UrlAndroidRequest;
import com.example.demo.domain.*;
import com.example.demo.repo.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UrlProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

//NOTE: THIS API CONTROLLER WAS PRIORITY RE-WRITTEN FOR ANDROID, NOT FOR WEB.
// PLEASE DON'T USE FOR WEB OR EDIT THEM TO AVOID EXCEPTION ERROR FOR BOTH WEB OR APP
// UPDATED: EXCEPT REVENUE API CAN USE FOR BOTH WEB AND APP


@RestController
@Validated
@RequestMapping(value = "/api/android")
public class AndroidApiController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AndroidOrderRepo androidOrderRepo;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LinkTypeRepository linkTypeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UrlProductService urlProductService;
    @Autowired
    private UrlProductRepository urlRepo;

    // URL PRODUCT API FOR ANDROID
    //show all url api for URL product
    @GetMapping("/list/{username}")
    public ResponseEntity<?> ShowAllByUsername(@PathVariable String username) {
        return ResponseEntity.ok(urlProductService.ShowAllByUser(username));
    }
    //show all link type for dropdown list
    @GetMapping("/listLinkType")
    public ResponseEntity<?> ShowAllLinkType() {
        return ResponseEntity.ok((List<LinkType>)linkTypeRepository.findAll());
    }

    //add/update url for app
    @PostMapping("/addUrl")
    public ResponseEntity<?> addUrl(@Valid @RequestBody UrlAndroidRequest request){//need @request body to pass json data
        var linktype_id = linkTypeRepository.findById(request.getType_id());
        var user = userRepo.findById(request.getUser_id());
        var product_id = productRepository.findProductByUser(user.get());
        UrlProduct urlProduct = new UrlProduct();
        urlProduct.setName(request.getName());
        urlProduct.setUrl(request.getUrl());
        urlProduct.setLinkType(linktype_id.get());
        urlProduct.setProduct(product_id);
        urlProduct.setUser(user.get());
        urlProductService.addUrl(urlProduct);
        return ResponseEntity.ok(urlProduct);
    }

    //update url
    @PostMapping("/updateUrl/{url_id}")
    public ResponseEntity<?> updateUrl(@PathVariable("url_id") Long url_id, @Valid @RequestBody UrlAndroidRequest request){//need @request body to pass json data
        var urlProduct = urlRepo.findById(url_id).get();

        var linktype_id = linkTypeRepository.findById(request.getType_id());
        urlProduct.setName(request.getName());
        urlProduct.setUrl(request.getUrl());
        urlProduct.setLinkType(linktype_id.get());
        urlProductService.addUrl(urlProduct);//update
        return ResponseEntity.ok(urlProduct);
    }

    //delete url
    @GetMapping("/deleteUrl/{url_id}")
    public ResponseEntity<?> deleteUrlById(@PathVariable("url_id") Long url_id) {
        String response = urlProductService.deleteUrl(url_id);
        if(response=="OK"){
            return new ResponseEntity("Success", HttpStatus.OK);
        }else{
            return new ResponseEntity("ID Not Found", HttpStatus.NOT_FOUND);
        }
    }

    //USER API FOR ANDROID
    //update profile user
    @PostMapping("/updateProfile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable("id") Long id, @Valid @RequestBody UpdateProfile request) throws IOException {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setFullname(request.getFullname());
            u.setEmail(request.getEmail());
            u.setPhone(request.getPhone());
            u.setAddress(request.getAddress());
            u.setDateOfbirth(request.getDateOfbirth());
            u.setGender(request.getGender());
            u.setDescription(request.getDescription());

            userRepo.save(u);
            return ResponseEntity.ok(u);

        } else {
            throw new ApiRequestException( "Save change fails");
        }
    }

    //ORDER API FOR ANDROID
    //get order list by username
    @GetMapping("/orderListByUsername/{username}")
    public ResponseEntity<?> getOrdersByUsername(@PathVariable("username") String username) {
        List<Orders> ordersList = androidOrderRepo.findOrdersByUser(userRepo.findByUsername(username).get());
        return ResponseEntity.ok(ordersList);
    }

    //PRODUCT API FOR ANDROID
    //get product by username for android
    @GetMapping("/getProduct/{username}")
    public ResponseEntity<?> getProduct(@PathVariable String username) {
        var user = userRepo.findByUsername(username);
        Product product = productRepository.findProductByUser(user.get());
        return ResponseEntity.ok(product);
    }
}
