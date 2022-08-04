package com.example.demo.Controllers;

import com.example.demo.Payload.Request.AddCategoryRequest;
import com.example.demo.Payload.Request.Chart;
import com.example.demo.Payload.Request.CreateOrderRequest;
import com.example.demo.domain.Orders;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(orderService.showAll());
    }
    @GetMapping("/getCharts")
    public ResponseEntity<?> getCharts() {
        List<List<Integer>> value = orderRepository.getPriceByOrder();
        List<Chart> c = new ArrayList<>();
        for (int i = 0 ; i < value.size() ; i++){
            Chart chart = new Chart();
            for (int j = 0 ; j < value.get(i).size() ; j++){
                if (j == 0){

                    chart.setMonth((int) value.get(i).get(j));
                }
                if (j == 1){
                    chart.setPrice((int) value.get(i).get(j));
                }
            }
            c.add(chart);
        }
        return ResponseEntity.ok(c);

    }

    @GetMapping("/getCharts/{categoryId}")
    public ResponseEntity<?> getChartsCategory(@PathVariable Long categoryId) {
        if (categoryId == 0){
            List<List<Integer>> values = orderRepository.getPriceByOrder();
            List<Chart> c = new ArrayList<>();
            for (int i = 0 ; i < values.size() ; i++){
                Chart chart = new Chart();
                for (int j = 0 ; j < values.get(i).size() ; j++){
                    if (j == 0){
                        chart.setMonth((int) values.get(i).get(j));
                    }
                    if (j == 1){
                        chart.setPrice((int) values.get(i).get(j));
                    } if (j == 2){
                        chart.setYear((int) values.get(i).get(j));
                    }
                }
                c.add(chart);
            }


            return ResponseEntity.ok(c);
        }else{
            List<List<Integer>> value = orderRepository.getPriceByOrderCategory(categoryId);
            List<Chart> c = new ArrayList<>();
            for (int i = 0 ; i < value.size() ; i++){
                Chart chart = new Chart();
                for (int j = 0 ; j < value.get(i).size() ; j++){
                    if (j == 0){

                        chart.setMonth((int) value.get(i).get(j));
                    }
                    if (j == 1){
                        chart.setPrice((int) value.get(i).get(j));
                    } if (j == 2){
                        chart.setYear((int) value.get(i).get(j));
                    }
                }
                c.add(chart);
            }
            return ResponseEntity.ok(c);        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getDetails(@PathVariable Long id) {
        Orders order = orderService.details(id);
        if(order == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found");
        }
        else{
            return ResponseEntity.ok(order);
        }
    }

    @PostMapping(value="/add" , consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody CreateOrderRequest createOrderRequest) throws IOException, WriterException, MessagingException {

        Orders response = orderService.create(createOrderRequest);
        return ResponseEntity.ok(response);

    }

    @GetMapping(path = "/confirmOrder")
    public RedirectView confirm(@RequestParam("id") Long id)  {

        return orderService.confirmToken(id);
    }

    @GetMapping("/nextProcess/{id}")
    public ResponseEntity<?> nextProcess(@PathVariable Long id) throws MessagingException {
        Boolean next = orderService.nextProcess(id);
        if(next == false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
        }
        else{
            return ResponseEntity.ok("success");
        }
    }

    @GetMapping("cancelOrder/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) throws MessagingException {
        Boolean cancelOrder = orderService.cancelOrder(id);
        if(cancelOrder == false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
        }
        else{
            return ResponseEntity.ok("success");
        }
    }


    @GetMapping("/MonthOrder/{Month}")
    public ResponseEntity<?> MonthOrder(@PathVariable int Month) {

        List<List<Integer>> values = orderRepository.getPriceMonthByOrder(Month);
        List<Chart> c = new ArrayList<>();

        for (int i = 0 ; i < values.size() ; i++){
            Chart chart = new Chart();
            for (int j = 0 ; j < values.get(i).size() ; j++){
                if (j == 0){

                    chart.setDay((int) values.get(i).get(j));
                }
                if (j == 1){
                    chart.setPrice((int) values.get(i).get(j));
                }
                if (j == 2){
                    chart.setMonth((int) values.get(i).get(j));
                }
                if (j == 3){
                    chart.setYear((int) values.get(i).get(j));
                }
            }
            c.add(chart);
        }


        return ResponseEntity.ok(c);

    }

    @GetMapping("/YearOrder")
    public ResponseEntity<?> YearOrder(@RequestParam(name = "d2", defaultValue = "2021-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date d2) {

        List<List<Integer>> values = orderRepository.getPriceByOrderS(d2);
        List<Chart> c = new ArrayList<>();

        for (int i = 0 ; i < values.size() ; i++){
            Chart chart = new Chart();
            for (int j = 0 ; j < values.get(i).size() ; j++){
                if (j == 0){

                    chart.setMonth((int) values.get(i).get(j));
                }
                if (j == 1){
                    chart.setPrice((int) values.get(i).get(j));
                }
                if (j == 2){
                    chart.setYear((int) values.get(i).get(j));
                }
            }
            c.add(chart);
        }


        return ResponseEntity.ok(c);

    }
}
