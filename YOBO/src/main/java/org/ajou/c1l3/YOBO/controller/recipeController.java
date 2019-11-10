package org.ajou.c1l3.YOBO.controller;

import com.mongodb.operation.FindOperation;
import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.domain.simpleRecipe;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class recipeController {

    //test
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    CurrentRecipeRepository currentRecipeRepository;

    @GetMapping("/yobo/recipe/search/{recipeName}")
    public YoboRecipe getYoboRecipe(@PathVariable String recipeName){
        Query query = Query.query(where("recipe_name").is(recipeName));
        return mongoTemplate.findOne(query, YoboRecipe.class);
    }


    @GetMapping("/yobo/recipe/getRecipeList/")
    public  List<simpleRecipe> getRecipeList(@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*10;
        SkipOperation skip= Aggregation.skip(skipn);
        LimitOperation limit =Aggregation.limit(pageSize);
        Aggregation aggregation = Aggregation.newAggregation(skip,limit);
        AggregationResults<simpleRecipe> results = mongoTemplate.aggregate(aggregation,"Recipe",simpleRecipe.class);
        List<simpleRecipe> list = results.getMappedResults();  //결과
        return list;
    }

    @GetMapping("/yobo/recipe/getListbyCate/")
    public  List<simpleRecipe> getListbyCate(@RequestParam("cate") String cate, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*10;
        SkipOperation skip= Aggregation.skip(skipn);
        LimitOperation limit =Aggregation.limit(pageSize);
        MatchOperation match = Aggregation.match( Criteria.where("category").in(cate));
        Aggregation aggregation = Aggregation.newAggregation(skip,limit,match);
        AggregationResults<simpleRecipe> results = mongoTemplate.aggregate(aggregation,"Recipe",simpleRecipe.class);
        List<simpleRecipe> list = results.getMappedResults();  //결과
        return list;
    }

    @GetMapping("/yobo/recipe/getListbyUid/")
    public  List<simpleRecipe> getListbyUid(@RequestParam("uid") String uid, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*10;
        SkipOperation skip= Aggregation.skip(skipn);
        LimitOperation limit =Aggregation.limit(pageSize);
        MatchOperation match = Aggregation.match( Criteria.where("writer_id").is(uid));
        Aggregation aggregation = Aggregation.newAggregation(skip,limit,match);
        AggregationResults<simpleRecipe> results = mongoTemplate.aggregate(aggregation,"Recipe",simpleRecipe.class);
        List<simpleRecipe> list = results.getMappedResults();  //결과
        return list;
    }
    @GetMapping("/yobo/recipe/getListbyDid/")
    public  List<simpleRecipe> getListbyDid(@RequestParam("Did") String Did, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*10;
        SkipOperation skip= Aggregation.skip(skipn);
        LimitOperation limit =Aggregation.limit(pageSize);
        MatchOperation match = Aggregation.match( Criteria.where("_id").is(Did));
        Aggregation aggregation = Aggregation.newAggregation(skip,limit,match);
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
