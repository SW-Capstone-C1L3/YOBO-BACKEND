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
        private String product_image;
        private String product_description;
        private int qty;
        private int product_price;
        private String product_name;
        private String company_id;
    }

}
