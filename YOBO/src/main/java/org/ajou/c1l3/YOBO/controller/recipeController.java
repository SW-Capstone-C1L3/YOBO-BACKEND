package org.ajou.c1l3.YOBO.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.operation.FindOperation;
import com.mongodb.util.JSON;
import jdk.nashorn.internal.parser.JSONParser;
import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.domain.simpleRecipe;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class recipeController {
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();

    private  String SAVE_PATH = s+"/Recipe/upload";
    private  String PREFIX_URL = s+"/Recipe/upload/";
    //test
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    CurrentRecipeRepository currentRecipeRepository;

    @GetMapping("/yobo/recipe/search/")
    public List<simpleRecipe> getYoboRecipe(@RequestParam("recipeName") String recipeName ,@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("recipe_name").regex(recipeName));
        query.limit(pageSize);
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, simpleRecipe.class);
    }


    @GetMapping("/yobo/recipe/getRecipeList/")
    public  List<simpleRecipe> getRecipeList(@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*pageSize;
        SkipOperation skip= Aggregation.skip(skipn);
        LimitOperation limit =Aggregation.limit(pageSize);
        Aggregation aggregation = Aggregation.newAggregation(skip,limit);
        AggregationResults<simpleRecipe> results = mongoTemplate.aggregate(aggregation,"Recipe",simpleRecipe.class);
        List<simpleRecipe> list = results.getMappedResults();  //결과
        return list;
    }

    @GetMapping("/yobo/recipe/getListbyCate/")
    public  List<simpleRecipe> getListbyCate(@RequestParam("cate") String cate, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*10;
        Query query = Query.query(where("category").regex(cate));
        query.limit(pageSize);
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, simpleRecipe.class);

    }

    @GetMapping("/yobo/recipe/getListbyUid/")
    public  List<simpleRecipe> getListbyUid(@RequestParam("uid") String uid, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("writer_id").is(uid));
        query.limit(pageSize);
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, simpleRecipe.class);
    }
    @GetMapping("/yobo/recipe/getRecipebyDid/")
    public  YoboRecipe getListbyDid(@RequestParam("Did") String Did){
        Query query = Query.query(where("_id").is(Did));
        return mongoTemplate.findOne(query, YoboRecipe.class);
    }
    @RequestMapping(value = "/yobo/recipe/getImage/",method= RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestParam String filePath) throws IOException {
        RandomAccessFile f = new RandomAccessFile(filePath, "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/yobo/recipe/createRecipe", consumes = {"multipart/form-data"})
    public int createRecipe(@RequestParam("img")  MultipartFile[] files,@RequestParam  String recipe){
        ObjectMapper objectMapper =new ObjectMapper();
        YoboRecipe recipe1;
        String url = null;
        int test[] ={0,1};
        System.out.println(test);
        System.out.println(files);
        //경로 확인 용
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        System.out.println(files.length);
        try {
            recipe1 = objectMapper.readValue(recipe, YoboRecipe.class);
            int imgcnt=0;
            for(int i=0;i<recipe1.getCooking_description().length;i++){
                if(recipe1.getCooking_description()[i].getImage().equals("NULL"))continue;
                else if(recipe1.getCooking_description()[i].getImage().equals("NULL")==false){
                    System.out.println(recipe1.getCooking_description()[i].getImage());
                    System.out.println(files[imgcnt]);
                    String originFilename = files[imgcnt].getOriginalFilename();
                    String extName
                            = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
                    Long size = files[imgcnt].getSize();
                    // 서버에서 저장 할 파일 이름
                    String saveFileName = genSaveFileName(extName);
                    System.out.println("originFilename : " + originFilename);
                    System.out.println("extensionName : " + extName);
                    System.out.println("size : " + size);
                    System.out.println("saveFileName : " + saveFileName);
                    writeFile(files[imgcnt], saveFileName);
                    url = PREFIX_URL + saveFileName;
                    recipe1.getCooking_description()[i].setImage(url);
                    imgcnt+=1;
                }

            }
            mongoTemplate.insert(recipe1);

        }catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
        return 1;

    }

    private String genSaveFileName(String extName) {
        String fileName = "";

        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += calendar.get(Calendar.MONTH);
        fileName += calendar.get(Calendar.DATE);
        fileName += calendar.get(Calendar.HOUR);
        fileName += calendar.get(Calendar.MINUTE);
        fileName += calendar.get(Calendar.SECOND);
        fileName += calendar.get(Calendar.MILLISECOND);
        fileName += extName;

        return fileName;
    }




    // 파일을 실제로 write 하는 메서드
    private boolean writeFile(MultipartFile multipartFile, String saveFileName)
            throws IOException{
        boolean result = false;

        byte[] data = multipartFile.getBytes();
        FileOutputStream fos = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
        fos.write(data);
        fos.close();

        return result;
    }

}
