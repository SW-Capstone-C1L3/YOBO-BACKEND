package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboReport_log;
import org.ajou.c1l3.YOBO.domain.YoboTransaction_log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class transactionController {
    @Autowired
    MongoTemplate mongoTemplate;
    @PostMapping("/yobo/transaction/createtransaction")
    public int createTransaction(@RequestParam("product_id") String product_id, @RequestParam("user_id") String user_id,
                                 @RequestParam("product_qty") double product_qty, @RequestParam("company_id") String company_id,
                                 @RequestParam("invoice_company") String invoice_company, @RequestParam("transaction_status") String transaction_status,
                                 @RequestParam("user_address") String user_address, @RequestParam("user_phone_num") int user_phone_num,
                                 @RequestParam("invoice_number") int invoice_number){
        YoboTransaction_log transaction=null;
        transaction.setCompany_id(company_id);
        transaction.setInvoice_company(invoice_company);
        transaction.setInvoice_number(invoice_number);
        transaction.setProduct_id(product_id);
        transaction.setProduct_qty(product_qty);
        transaction.setTransaction_status(transaction_status);
        transaction.setUser_address(user_address);
        transaction.setUser_id(user_id);
        transaction.setUser_phone_num(user_phone_num);
        try {
            mongoTemplate.insert(transaction);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }




}
