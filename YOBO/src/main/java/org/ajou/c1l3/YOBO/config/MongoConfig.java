package org.ajou.c1l3.YOBO.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;

@Configuration
public class MongoConfig {
    private String password="userpwd";
    @Bean
    public MongoTemplate createMongoTemplate() {
        MongoCredential credential = MongoCredential.createCredential("YOBO_USER", "YOBO", password.toCharArray());
        //auth code
        return new MongoTemplate(new MongoClient(new ServerAddress("45.119.146.82", 27017), Arrays.asList(credential)), "YOBO");
    }
}

