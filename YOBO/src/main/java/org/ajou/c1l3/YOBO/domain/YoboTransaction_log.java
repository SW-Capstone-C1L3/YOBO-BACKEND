package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="transaction_log")
public class YoboTransaction_log {
    private String product_id;
    private double product_qty;
    private String user_id;
    private String _id;
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public double getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(double product_qty) {
        this.product_qty = product_qty;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public int getUser_phone_num() {
        return user_phone_num;
    }

    public void setUser_phone_num(int user_phone_num) {
        this.user_phone_num = user_phone_num;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public int getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(int invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_company() {
        return invoice_company;
    }

    public void setInvoice_company(String invoice_company) {
        this.invoice_company = invoice_company;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    private String user_address;
    private int user_phone_num;
    private String transaction_status;
    private int invoice_number;
    private String invoice_company;
    private String company_id;
}
