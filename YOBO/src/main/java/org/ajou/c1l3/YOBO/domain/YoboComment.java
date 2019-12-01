package org.ajou.c1l3.YOBO.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Data
@Document(collection="Comments")
public class YoboComment {

    private String _id;
    private String comments;
    private String user_id;
    private String user_name;

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;
    private String recipe_id;
}
