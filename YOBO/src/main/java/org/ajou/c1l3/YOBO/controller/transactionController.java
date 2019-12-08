package org.ajou.c1l3.YOBO.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ajou.c1l3.YOBO.domain.YoboComment;
import org.ajou.c1l3.YOBO.domain.YoboReport_log;
import org.ajou.c1l3.YOBO.domain.YoboTransaction_log;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class transactionController implements  Cloneable {

    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/yobo/transaction/modifystatus")
    public int modifystatus(@RequestParam("Did") String Did,@RequestParam("invoice_company") String invoice_company,@RequestParam("invoice_number") String invoice_number,@RequestParam("transaction_status") String transaction_status){
        try {
            Query query=query(where("_id").is(Did));
            Update update =new Update();
            update.set("invoice_company",invoice_company);
            update.set("invoice_number",invoice_number);
            update.set("transaction_status",transaction_status);
            mongoTemplate.findAndModify(
                    query
                    ,update
                    , YoboTransaction_log.class);
            return 1;
        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping("/yobo/transaction/createtransaction")
    public int createTransaction(@RequestParam("transcationLog") String trasctionLog) throws IOException {
        List<String> cid = new ArrayList();

        System.out.println(trasctionLog);
        TimeZone time;
        ObjectMapper objectMapper =new ObjectMapper();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.setTime(date);
        YoboTransaction_log tmptransaction=new YoboTransaction_log();

        tmptransaction = objectMapper.readValue(trasctionLog, YoboTransaction_log.class);
        tmptransaction.setTimestamp(new Timestamp(date.getTime()));


        int flag=0;

        for(int i=0;i<tmptransaction.getProducts().length;i++){
            for(int j=0;j<cid.size();j++){
                if(cid.get(j).equals(tmptransaction.getProducts()[i].getCompany_id())){
                    flag=1;
                }
            }
            if(flag==0){
                cid.add(tmptransaction.getProducts()[i].getCompany_id());
                System.out.println(tmptransaction.getProducts()[i].getCompany_id());
            }
            flag=0;
        }
        for(int i=0;i<cid.size();i++){
            YoboTransaction_log tmptransaction2=new YoboTransaction_log();
            tmptransaction2 =objectMapper.readValue(trasctionLog, YoboTransaction_log.class);
            tmptransaction2.setTimestamp(tmptransaction.getTimestamp());
            List<YoboTransaction_log.product> product= new ArrayList();
            double total=0;

            for(int j=0;j<tmptransaction.getProducts().length;j++){
                if(cid.get(i).equals(tmptransaction.getProducts()[j].getCompany_id())){
                    total+=tmptransaction.getProducts()[j].getPrice()*tmptransaction.getProducts()[j].getProduct_qty();
                    product.add(tmptransaction.getProducts()[j]);
                }
            }
            System.out.println("dd");
            YoboTransaction_log.product[] simpleArray = new YoboTransaction_log.product[ product.size() ];
            product.toArray( simpleArray );
            tmptransaction2.setProducts(simpleArray);
            tmptransaction2.setTotal_price(total);

            tmptransaction2.setCompany_id(product.get(0).getCompany_id());
            try {
                    mongoTemplate.insert(tmptransaction2);

            }catch (Exception e) {
                System.out.println(tmptransaction);
                e.printStackTrace();
                return -1;
            }
        }
    return 1;

    }
    @GetMapping("/yobo/transaction/getByUid")
    public List<YoboTransaction_log> getByUid(@RequestParam("Uid") String Did, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("user_Did").is(Did));
        return mongoTemplate.find(query,YoboTransaction_log.class);
    }
    @GetMapping("/yobo/transaction/getByCid")
    public List<YoboTransaction_log> getByCid(@RequestParam("Cid") String Cid, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("company_id").is(Cid));
        return mongoTemplate.find(query,YoboTransaction_log.class);
    }
    @GetMapping("/yobo/transaction/getByDid")
    public YoboTransaction_log getByDid(@RequestParam("Did") String Did, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query=Query.query(where("_id").is(Did));
        return mongoTemplate.findOne(query,YoboTransaction_log.class);
    }

}
