package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection="Recipe")
public class YoboRecipe{
    private String recipe_name;
    private String writer_id;
    private int difficulty;
    private double rating;
    private String[] category;
    private  int serving;
    private descrition[] cooking_description;
    private ingredients[] main_cooking_ingredients;
    private ingredients[] sub_cooking_ingredients;

    @Data
    public static class descrition{
        private String description;
        private String image;

    }
    @Data
    public static class ingredients{
        private String ingredients_name;
        private int  qty;
        private String unit;
    }

}
