package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboBasket;
import org.ajou.c1l3.YOBO.domain.YoboComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class commentsController {

    @Autowired
    MongoTemplate mongoTemplate;

//    @PostMapping(value = "/yobo/basket/createBasket")
//    public int createBasket(@RequestParam("User_id") YoboComment User_id) {
//        try {
//
//            YoboBasket basket=new YoboBasket(User_id,null);
//            mongoTemplate.insert(basket);
//            return 1;
//
//        }catch (Exception e) {
//            System.out.println(User_id);
//            e.printStackTrace();
//            return -1;
//        }
//    }

}
