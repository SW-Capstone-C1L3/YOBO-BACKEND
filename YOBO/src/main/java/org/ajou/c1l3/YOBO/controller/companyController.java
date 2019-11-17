package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboComment;
import org.ajou.c1l3.YOBO.domain.YoboCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class companyController {

    //test
    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping(value = "/yobo/company/createcompany")
    public int createCompany(@RequestBody YoboCompany company) {
        try {
            mongoTemplate.insert(company);
            return 1;

        }catch (Exception e) {
            System.out.println(company);
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping(value = "/yobo/company/updateCompany")
    public int updateCompany(@RequestBody YoboCompany company) {
        try {
            mongoTemplate.insert(company);
            return 1;
        }catch (Exception e) {
            System.out.println(company);
            e.printStackTrace();
            return -1;
        }
    }

}
