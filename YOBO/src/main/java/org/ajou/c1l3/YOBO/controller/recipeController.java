package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.domain.simpleRecipe;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class recipeController {

    //test
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    CurrentRecipeRepository currentRecipeRepository;

    @GetMapping("/yobo/recipe/search/{recipeName}")
    public YoboRecipe getYoboRecipe(@PathVariable String recipeName){
        Query query = Query.query(Criteria.where("recipe_name").is(recipeName));
        return mongoTemplate.findOne(query, YoboRecipe.class);
    }

    @GetMapping("/yobo/recipe/getRecipeList/{pageNum}")
    public  List<simpleRecipe> getRecipeList(@PathVariable int pageNum){
        int skipn=pageNum*10;
        SkipOperation skip= Aggregation.skip(skipn);
        LimitOperation limit =Aggregation.limit(10);
        Aggregation aggregation = Aggregation.newAggregation(skip,limit);
        AggregationResults<simpleRecipe> results = mongoTemplate.aggregate(aggregation,"Recipe",simpleRecipe.class);
        List<simpleRecipe> list = results.getMappedResults();  //결과
        return list;
    }

    @PostMapping("/yobo/recipe/createRecipe")
    public YoboRecipe createRecipe(@RequestBody YoboRecipe recipe){
        mongoTemplate.insert(recipe);
        return recipe;
    }




}
