package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="Product")
public class simpleProduct {
    private String _id;
    private String product_name;
    private int product_price;
    private String product_category;
    private int product_qty;
    private String product_unit;
    private String product_description;
    private String provided_company_id;
    private String company_name;
    private String product_image;
}
