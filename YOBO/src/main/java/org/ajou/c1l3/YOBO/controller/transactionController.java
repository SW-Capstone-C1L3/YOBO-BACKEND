package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboReport_log;
import org.ajou.c1l3.YOBO.domain.YoboTransaction_log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class transactionController {

    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/yobo/transaction/createtransaction")
    public int createTransaction(@RequestBody YoboTransaction_log trasctionLog){

        try {
            mongoTemplate.insert(trasctionLog);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }
    @GetMapping("/yobo/transaction/getByUid")
    public List<YoboTransaction_log> getByUid(@RequestParam("Uid") String Uid, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("user_id").is(Uid));
        return mongoTemplate.find(query,YoboTransaction_log.class);
    }
    @GetMapping("/yobo/transaction/getByCid")
    public List<YoboTransaction_log> getByCid(@RequestParam("Cid") String Cid, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("company_id").is(Cid));
        return mongoTemplate.find(query,YoboTransaction_log.class);
    }


}
