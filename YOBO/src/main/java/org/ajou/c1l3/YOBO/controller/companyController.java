package org.ajou.c1l3.YOBO.controller;

import org.ajou.c1l3.YOBO.domain.YoboComment;
import org.ajou.c1l3.YOBO.domain.YoboCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@CrossOrigin
@RestController
public class companyController {

    //test
    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping(value = "/yobo/company/createcompany")
    public int createCompany(@RequestParam("company") YoboCompany company) {
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

    @GetMapping(value = "/yobo/company/getCompanyByName")
    public YoboCompany getCompanyByName(@RequestParam("name") String name) {
        Query query = Query.query(where("company_name").is(name));
        return mongoTemplate.findOne(query,YoboCompany.class);
    }
    @GetMapping(value = "/yobo/company/getCompanyByEmail")
    public YoboCompany getCompanyByEmail(@RequestParam("email") String email) {
        Query query = Query.query(where("company_ld").is(email));
        return mongoTemplate.findOne(query,YoboCompany.class);
    }
}
