package rocky.com.example.virtualbookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocky.com.example.virtualbookstore.module.bookstore_book;
import rocky.com.example.virtualbookstore.module.orderItem;
import rocky.com.example.virtualbookstore.repository.orderCriteriaRepository;

import java.util.List;

@RestController
@RequestMapping("/orderItem")
public class orderController {

    @Autowired
    orderCriteriaRepository orderCriteriaRepository;

    @GetMapping("/findOrderItem")
    public ResponseEntity<List<orderItem>> getOrderItem(){
//        List<orderItem> orderItemList = orderCriteriaRepository.findOrderItemTest();
        return ResponseEntity.status(200).body(orderCriteriaRepository.findOrderItemTest());
    }

    //create the object by entity
    //@PostMapping
}
