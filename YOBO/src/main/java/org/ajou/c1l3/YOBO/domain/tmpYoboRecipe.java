package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection="Recipe")
public class tmpYoboRecipe {
    public String get_id() {
        return _id;
    }

    private String _id;
    private String recipe_name;
    private String writer_id;
    private String writer_name;
    private String difficulty;
    private double rating;
    private String[] category;
    private String serving;
    private descrition[] cooking_description;
    private ingredients main_cooking_ingredients;
    private int conut;

    @Data
    public static class descrition{
        private String description;
        private String image;
    }

    @Data
    public static class ingredients{
        private String ingredients_name;
        private double  qty;
        private String unit;
    }



}
