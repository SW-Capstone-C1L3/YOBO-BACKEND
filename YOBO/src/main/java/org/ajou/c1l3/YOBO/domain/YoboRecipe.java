package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection="Recipe")
public class YoboRecipe{
    private String _id;
    private String recipe_name;
    private String writer_id;
    private String writer_name;
    private String difficulty;
    private double rating;
    private int rating_count;
    private String[] category;
    private String serving;
    private descrition[] cooking_description;
    private ingredients[] main_cooking_ingredients;
    private ingredients[] sub_cooking_ingredients;
    private rated_people[] rated;

    public void setRate_uid(rated_people[] rated) {
        this.rated = rated;
    }


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
    @Data
    public static class rated_people{
        private String uid;
        private double rate;

        public rated_people(String uid, double rate) {
            this.uid = uid;
            this.rate = rate;
        }
    }


}
