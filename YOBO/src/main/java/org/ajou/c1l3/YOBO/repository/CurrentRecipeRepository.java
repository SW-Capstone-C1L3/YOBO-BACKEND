package org.ajou.c1l3.YOBO.repository;

import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CurrentRecipeRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public YoboRecipe findCurrentRecipe(String recipeName) {
        Query query = Query.query(Criteria.where("name").is("11"));
        System.out.println(query);
        return mongoTemplate.findOne(query, YoboRecipe.class);
    }
}
