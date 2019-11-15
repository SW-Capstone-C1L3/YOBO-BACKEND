package org.ajou.c1l3.YOBO.domain;


import lombok.Data;

@Data
public class simpleBasket {

    public simpleBasket(String product_id, int qty) {
        Product_id = product_id;
        this.qty = qty;
    }

    private String Product_id;
    private int qty;
}
