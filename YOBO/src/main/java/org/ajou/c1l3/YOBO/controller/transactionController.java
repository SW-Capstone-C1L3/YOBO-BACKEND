package org.ajou.c1l3.YOBO.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ajou.c1l3.YOBO.domain.YoboComment;
import org.ajou.c1l3.YOBO.domain.YoboReport_log;
import org.ajou.c1l3.YOBO.domain.YoboTransaction_log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class transactionController {

    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/yobo/transaction/createtransaction")
    public int createTransaction(@RequestParam("transcationLog") String trasctionLog) throws IOException {
        YoboTransaction_log trans[];
        System.out.println(trasctionLog);
        TimeZone time;
        ObjectMapper objectMapper =new ObjectMapper();
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.setTime(date);
        YoboTransaction_log tmptransaction=new YoboTransaction_log();
        tmptransaction = objectMapper.readValue(trasctionLog, YoboTransaction_log.class);
        try {
            tmptransaction.setTimestamp(new Timestamp(date.getTime()));
            mongoTemplate.insert(tmptransaction);
            return 1;

        }catch (Exception e) {
            System.out.println(tmptransaction);
            e.printStackTrace();
            return -1;
        }

    }
    @GetMapping("/yobo/transaction/getByDid")
    public List<YoboTransaction_log> getByDid(@RequestParam("Did") String Did, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("user_Did").is(Did));
        return mongoTemplate.find(query,YoboTransaction_log.class);
    }
    @GetMapping("/yobo/transaction/getByCid")
    public List<YoboTransaction_log> getByCid(@RequestParam("Cid") String Cid, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("company_id").is(Cid));
        return mongoTemplate.find(query,YoboTransaction_log.class);
    }


}
