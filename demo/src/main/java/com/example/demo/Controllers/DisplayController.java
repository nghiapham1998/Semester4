package com.example.demo.Controllers;

import com.example.demo.Payload.Request.Chart;
import com.example.demo.Payload.Request.ChartProductAccess;
import com.example.demo.domain.Product;
import com.example.demo.domain.ProductAccessTime;
import com.example.demo.repo.ProductAccessTimeRepository;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/display")
public class DisplayController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductAccessTimeRepository productAccessTimeRepository;
    //Display Product
    @GetMapping("/{username}")
    public ResponseEntity<?> displayProduct(@PathVariable String username) {
        if(!userRepo.existsByUsername(username)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
        }
        if(!productRepository.existsByUser(userRepo.findByUsername(username).get())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
        }
        var user = userRepo.findByUsername(username);
        Product product = productRepository.findProductByUser(user.get());
        if(product.getStatus() ==1){
            //check Duration
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate date = LocalDate.now();
            String strDate = date.format(formatter);
            LocalDate parsedDate = LocalDate.parse(strDate, formatter);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate2 = dateFormat.format(product.getCreatedAt());
            LocalDate parsedDate2 = LocalDate.parse(strDate2, formatter);
            LocalDate expired = parsedDate2.plusYears(product.getYear());

            if(expired.isBefore(parsedDate)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");
            }

            //count visit
            product.setCount(product.getCount()+1);
           var p = productRepository.save(product);
            ProductAccessTime productAccessTime = new ProductAccessTime();
            productAccessTime.setProduct(product);
            productAccessTime.setCreate_at(new Date());
            productAccessTimeRepository.save(productAccessTime);
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fails");

    }

    @GetMapping("/chartProductAccess/{id}/{month}")
    public  ResponseEntity<?> getChartUserProductAccess(@PathVariable Long id ,@PathVariable int month){
        List<ChartProductAccess> c = new ArrayList<>();
            if(month == 0){
                List<List<Integer>> valuesS = productAccessTimeRepository.countProductAccessTimeByCreate_atMonth(7,id);
                for (int i = 0 ; i < valuesS.size() ; i++){
                    ChartProductAccess chart = new ChartProductAccess();
                    for (int j = 0 ; j < valuesS.get(i).size() ; j++){
                        if (j == 0){

                            chart.setDay((int) valuesS.get(i).get(j));
                        }
                        if (j == 1){
                            chart.setCount((int) valuesS.get(i).get(j));
                        }
                        if (j == 2){
                            chart.setYear((int) valuesS.get(i).get(j));
                        }
                    }
                    c.add(chart);






                }
            }else{
                List<List<Integer>> valuesS = productAccessTimeRepository.countProductAccessTimeByCreate_atMonth(month,id);
                for (int i = 0 ; i < valuesS.size() ; i++){
                    ChartProductAccess chart = new ChartProductAccess();
                    for (int j = 0 ; j < valuesS.get(i).size() ; j++){
                        if (j == 0){

                            chart.setDay((int) valuesS.get(i).get(j));
                        }
                        if (j == 1){
                            chart.setCount((int) valuesS.get(i).get(j));
                        }
                        if (j == 2){
                            chart.setYear((int) valuesS.get(i).get(j));
                        }
                    }
                    c.add(chart);






                }
            }

        return ResponseEntity.ok(c);
    }

    @GetMapping("/YearProductAccess/{productid}")
    public ResponseEntity<?> YearOrder(@PathVariable Long productid) {

        List<List<Integer>> values = productAccessTimeRepository.countProductAccessTimeByCreate_atYear(2022,productid);
        List<ChartProductAccess> c = new ArrayList<>();

        for (int i = 0 ; i < values.size() ; i++){
            ChartProductAccess chart = new ChartProductAccess();
            for (int j = 0 ; j < values.get(i).size() ; j++){
                if (j == 0){

                    chart.setCount((int) values.get(i).get(j));
                }
                if (j == 1){
                    chart.setYear((int) values.get(i).get(j));
                }
                if (j == 2){
                    chart.setMonth((int) values.get(i).get(j));
                }
            }
            c.add(chart);
        }


        return ResponseEntity.ok(c);

    }

}
