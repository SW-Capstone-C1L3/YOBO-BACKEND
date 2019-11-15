package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Data
@Document(collection="Comment")
public class YoboComment {

    private String _id;
    private String comments;
    private String user_id;
    private Timestamp timestamp;
    private String recipe_id;

}
