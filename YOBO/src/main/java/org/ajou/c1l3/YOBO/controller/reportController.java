package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboComment;
import org.ajou.c1l3.YOBO.domain.YoboReport_log;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@RestController
public class reportController {
    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/yobo/report/createReport")
    public int createReport(@RequestParam("Rid") String Rid,@RequestParam("report_type") int report_type,@RequestParam("comment_id") String comment_id,@RequestParam("user_id") String user_id){
        YoboReport_log report=null;
        report.setComment_id(comment_id);
        report.setRecipe_id(Rid);
        report.setReport_type(report_type);
        report.setUser_id(user_id);
        try {
            mongoTemplate.insert(report);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }


    @GetMapping("/yobo/report/getByRid")
    public List<YoboReport_log> getByRid(@RequestParam("Rid") String Rid,@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("recipe_id").is(Rid));
        query.with(new Sort(Sort.Direction.DESC, "timestamp"));
        return mongoTemplate.find(query,YoboReport_log.class);
    }
    @GetMapping("/yobo/report/getByUid")
    public List<YoboReport_log> getByUid(@RequestParam("user_id") String user_id,@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("user_id").is(user_id));
        query.with(new Sort(Sort.Direction.DESC, "timestamp"));

        return mongoTemplate.find(query,YoboReport_log.class);
    }
    @GetMapping("/yobo/report/getByDid")
    public List<YoboReport_log> getByDid(@RequestParam("comment_id") String comment_id,@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("comment_id").is(comment_id));
        query.with(new Sort(Sort.Direction.DESC, "timestamp"));

        return mongoTemplate.find(query,YoboReport_log.class);
    }
    @GetMapping("/yobo/report/getReport")
    public List<YoboReport_log> getReport(@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("recipe_id").all());
        query.with(new Sort(Sort.Direction.DESC, "timestamp"));

        return mongoTemplate.find(query,YoboReport_log.class);
    }


}
