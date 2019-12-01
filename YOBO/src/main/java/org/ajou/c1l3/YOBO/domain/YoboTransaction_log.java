package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document(collection="transaction_log")
public class YoboTransaction_log {

    private String user_Did;
    private String user_id;
    private String _id;
    private product[] products;
    private String user_address;
    private String user_phone_num;
    private String transaction_status;
    private String invoice_number;
    private String invoice_company;
    private String company_id;
    private double total_price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;

    @Data
    public static class product{
        private String product_id;
        private double  product_qty;
    }
}