package org.ajou.c1l3.YOBO.controller;
import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

@RestController
public class userController {

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/yobo/user/{User_id}")
    public YoboUser getUserInfo(@PathVariable String User_id){
        Query query = Query.query(Criteria.where("user_id").is(User_id));
        return mongoTemplate.findOne(query, YoboUser.class);
    }

    @PostMapping("/yobo/recipe/createUser")
    public YoboUser createUser(@RequestBody YoboUser user){
        mongoTemplate.insert(user);
        return user;
    }

}
