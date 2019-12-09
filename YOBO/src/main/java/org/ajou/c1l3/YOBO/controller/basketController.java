package org.ajou.c1l3.YOBO.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.ajou.c1l3.YOBO.domain.YoboBasket;
import org.ajou.c1l3.YOBO.domain.YoboProduct;
import org.ajou.c1l3.YOBO.domain.simpleBasket;
import org.ajou.c1l3.YOBO.domain.simpleRecipe;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

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
        Query tmpquery = query(where("user_id").is(User_id));
        YoboBasket yoboBasket = mongoTemplate.findOne(tmpquery,YoboBasket.class);
        if(yoboBasket==null){
            this.createBasket(User_id);
        }
        try {
            simpleBasket simpleBasket =new simpleBasket(Product_id,qty);
            Query query = new Query();
            query.addCriteria(
                    new Criteria().andOperator(
                            where("user_id").is(User_id),
                            where("basket").elemMatch(where("Product_id").is(Product_id)))
            );
            YoboBasket basket=mongoTemplate.findOne(query,YoboBasket.class);
            int count=0;

            if(basket!=null){
                for(int i=0;i<basket.getBasket().length;i++){
                    if(basket.getBasket()[i].getProduct_id().equals(Product_id)){
                        count=basket.getBasket()[i].getQty();
                        break;
                    }
                }
                this.updateBasket(User_id,Product_id,qty+count);
            }else{
                mongoTemplate.updateFirst(query(where("user_id").is(User_id)), new Update().push("basket", simpleBasket), YoboBasket.class);
            }

            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping(value = "/yobo/basket/updateBasket")
    public int updateBasket(@RequestParam("User_id") String User_id,@RequestParam("Product_id") String Product_id,@RequestParam("qty") int qty) {
        try {
            simpleBasket simpleBasket =new simpleBasket(Product_id,qty);
            Query query = new Query();
            query.addCriteria(
                    new Criteria().andOperator(
                            where("user_id").is(User_id),
                            where("basket").elemMatch(where("Product_id").is(Product_id)))
            );
            mongoTemplate.findAndModify(
                    query
                    ,new Update().set("basket.$",simpleBasket)
                    ,YoboBasket.class);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }
    @PostMapping(value = "/yobo/basket/DeleteBasket")
    public int DeleteBasket(@RequestParam("User_id") String User_id,@RequestParam("Product_id") String Product_id) {
        try {
            Query query = new Query();
            query.addCriteria(
                    new Criteria().andOperator(
                            where("user_id").is(User_id))
            );
            Update update = new Update().pull("basket", new BasicDBObject("Product_id", Product_id));
            mongoTemplate.updateMulti(query, update, YoboBasket.class);

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
            YoboBasket yoboBasket = mongoTemplate.findOne(query,YoboBasket.class);
            if(yoboBasket==null){
                this.createBasket(User_id);
            }
            yoboBasket = mongoTemplate.findOne(query,YoboBasket.class);
            System.out.println(yoboBasket);
            for(YoboBasket.product basket:yoboBasket.getBasket()){
                Query query2 = query(where("_id").is(basket.getProduct_id()));
                YoboProduct yoboProduct =mongoTemplate.findOne(query2,YoboProduct.class);
                if(yoboProduct==null){
                    this.DeleteBasket(User_id,basket.getProduct_id());
                    continue;
                }
                System.out.println(yoboProduct);
                basket.setProduct_description(yoboProduct.getProduct_description());
                basket.setProduct_image(yoboProduct.getProduct_image());
                basket.setProduct_price(yoboProduct.getProduct_price());
                basket.setProduct_name(yoboProduct.getProduct_name());
                basket.setCompany_id(yoboProduct.getProvided_company_id());
            }
            return yoboBasket;
    }

}
