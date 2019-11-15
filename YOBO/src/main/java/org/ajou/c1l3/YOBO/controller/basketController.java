package org.ajou.c1l3.YOBO.controller;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.ajou.c1l3.YOBO.domain.YoboBasket;
import org.ajou.c1l3.YOBO.domain.simpleBasket;
import org.ajou.c1l3.YOBO.domain.simpleRecipe;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class basketController {

    //test
    @Autowired
    MongoTemplate mongoTemplate;


    @PostMapping(value = "/yobo/basket/createBasket")
    public int createBasket(@RequestParam("User_id") String User_id) {
        try {

            YoboBasket basket=new YoboBasket(User_id,null);
            mongoTemplate.insert(basket);
            return 1;

        }catch (Exception e) {
            System.out.println(User_id);
            e.printStackTrace();
            return -1;
        }
    }


    @PostMapping(value = "/yobo/basket/insertBasket")
    public int insertBasket(@RequestParam("User_id") String User_id,@RequestParam("Product_id") String Product_id,@RequestParam("qty") int qty) {
        try {
            simpleBasket simpleBasket =new simpleBasket(Product_id,qty);
            mongoTemplate.updateFirst(query(where("user_id").is(User_id)), new Update().push("basket", simpleBasket), YoboBasket.class);

            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }


    @GetMapping(value = "/yobo/basket/getBasket")
    public YoboBasket getBasket(@RequestParam("User_id") String User_id) {
            Query query = query(where("user_id").is(User_id));
            return mongoTemplate.findOne(query, YoboBasket.class);
    }

}
