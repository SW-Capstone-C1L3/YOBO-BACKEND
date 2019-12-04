package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection="User")
public class YoboUser{
    private String _id;
    private String user_id;
    private String user_name;
    private int user_age;
    private String user_phone_num;
    private String[] interest_category;
    private int user_grade;
    private String user_email;
    private String[] user_address;
    private String[] recipe_shotcut;

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

}

