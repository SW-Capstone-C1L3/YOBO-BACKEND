package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="Company")
public class YoboCompany {
    private String _id;
    private String company_name;
    private String company_phone_num;
    private double company_rating;
    private String company_location;
    private String company_ld;

}
