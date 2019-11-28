package org.ajou.c1l3.YOBO.controller;
import org.ajou.c1l3.YOBO.domain.YoboBasket;
import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class userController {

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/yobo/user/getbyEmail")
    public YoboUser getUserInfo(@RequestParam("Emai") String user_email){
        Query query = query(where("user_email").is(user_email));
        System.out.println("이메일"+user_email);
        YoboUser user=mongoTemplate.findOne(query, YoboUser.class);
        System.out.println(user);
        return user;
    }

    @GetMapping("/yobo/user/getbyDid")
    public YoboUser getUserbyDid(@RequestParam("Did") String Did){
        Query query = query(where("_id").is(Did));
        YoboUser user=mongoTemplate.findOne(query, YoboUser.class);
        System.out.println(user);
        return user;
    }

    @PostMapping("/yobo/recipe/createUser")
    public YoboUser createUser(@RequestBody YoboUser user){
        basketController basketController = new basketController();
        basketController.createBasket(user.getUser_id());
        mongoTemplate.insert(user);
        return user;
    }


    @PostMapping("/yobo/recipe/addShortCut")
    public int addShortCut(@RequestParam("Uid") String Uid, @RequestParam("Rid") ObjectId Rid){
        try {
            mongoTemplate.updateFirst(query(where("_id").is(Uid)), new Update().push("recipe_shotcut", Rid), YoboUser.class);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping("/yobo/recipe/DeleteShortCut")
    public int DeleteShortCut(@RequestParam("Uid") String Uid, @RequestParam("Rid") ObjectId Rid){
        try {
            Query query = query( where("_id").is(Uid));
            Update update = new Update().pull("recipe_shotcut", Rid);
            mongoTemplate.updateMulti(query, update, YoboUser.class);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }


}
