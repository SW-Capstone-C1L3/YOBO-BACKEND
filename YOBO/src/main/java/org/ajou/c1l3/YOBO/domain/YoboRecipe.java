package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection="YoboRecipe")
public class YoboRecipe{
    private String name;
    private String kind;
}
