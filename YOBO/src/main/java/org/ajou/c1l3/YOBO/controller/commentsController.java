package org.ajou.c1l3.YOBO.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import org.ajou.c1l3.YOBO.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class commentsController {

    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping(value = "/yobo/comments/createcomments")
    public int createComments(@RequestParam("comments") String comments) throws IOException {
        System.out.println(comments);
        TimeZone time;
        ObjectMapper objectMapper =new ObjectMapper();
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.setTime(date);
        YoboComment tmpcomment=new YoboComment();
        tmpcomment = objectMapper.readValue(comments, YoboComment.class);
        try {
            tmpcomment.setTimestamp(new Timestamp(date.getTime()));
            mongoTemplate.insert(tmpcomment);
            return 1;

        }catch (Exception e) {
            System.out.println(comments);
            e.printStackTrace();
            return -1;
        }
    }


    @GetMapping(value = "/yobo/comments/getCommentsbyRId")
    public List<YoboComment> getCommentsbyRId(@RequestParam("RId") String RId ,@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("recipe_id").is(RId));
        query.limit(pageSize);
        query.skip(pageNum*pageSize);
        query.with(new Sort(Sort.Direction.ASC, "timestamp"));
        return mongoTemplate.find(query, YoboComment.class);
    }

    @PostMapping(value = "/yobo/comments/updateComments")
    public int updateComments(@RequestParam("_id") String _id,@RequestParam("comments") String comments) {
        try {
            Query query = new Query();
            query.addCriteria(
                    new Criteria().where("_id").is(_id)
            );
            mongoTemplate.findAndModify(
                    query
                    ,new Update().set("comments",comments)
                    ,YoboComment.class);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping(value = "/yobo/comments/getCommentsbyUId")
    public List<YoboComment> getCommentsbyUId(@RequestParam("UId") String UId ,@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("user_id").is(UId));
        query.limit(pageSize);
        query.skip(pageNum*pageSize);
        query.with(new Sort(Sort.Direction.ASC, "timestamp"));
        return mongoTemplate.find(query, YoboComment.class);
    }
}
