package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

@RestController
public class recipeController {

    //test
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    CurrentRecipeRepository currentRecipeRepository;

    @GetMapping("/yobo/recipe/{recipeName}")
    public YoboRecipe getYoboRecipe(@PathVariable String recipeName){
        Query query = Query.query(Criteria.where("recipe_name").is(recipeName));
        return mongoTemplate.findOne(query, YoboRecipe.class);
    }

    @PostMapping("/yobo/recipe/createRecipe")
    public YoboRecipe createRecipe(@RequestBody YoboRecipe recipe){
        mongoTemplate.insert(recipe);
        return recipe;
    }



}
