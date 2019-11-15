package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="Basket")
public class YoboBasket {

    public YoboBasket(String user_id, product[] basket) {
        this.user_id = user_id;
        this.basket = basket;
    }

    private String user_id;
    private product[] basket;

    @Data
    public static class product{
        private String Product_id;
        private int qty;
    }
}
