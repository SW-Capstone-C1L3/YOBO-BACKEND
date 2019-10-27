package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="User")
public class YoboUser{
    private String user_id;
    private String user_name;
    private int user_age;
    private int user_phone_num;
    private int user_grade;
    private String user_email;
    private String[] user_address;
    private String[] recipe_shotcut;

}

