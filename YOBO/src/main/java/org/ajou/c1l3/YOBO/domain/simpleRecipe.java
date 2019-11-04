package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="Recipe")
public class simpleRecipe{
    private String _id;
    private String recipe_name;
    private String writer_id;
    private int difficulty;
    private double rating;
    private String[] category;
    private  int serving;



}
