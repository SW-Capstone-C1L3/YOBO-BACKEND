package org.ajou.c1l3.YOBO.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.operation.FindOperation;
import com.mongodb.util.JSON;
import jdk.nashorn.internal.parser.JSONParser;
import org.ajou.c1l3.YOBO.domain.*;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.ajou.c1l3.YOBO.controller.userController;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
    @GetMapping("/yobo/recipe/getSRecipebyDid/")
    public  simpleRecipe getSRecipebyDid(@RequestParam("Did") String Did){
        Query query = Query.query(where("_id").is(Did));
        return mongoTemplate.findOne(query, simpleRecipe.class);
    }
    @DeleteMapping("/yobo/recipe/DeleteDid/")
    public  int DeleteDid(@RequestParam("Did") String Did){
        Query query = Query.query(where("_id").is(Did));
        try {
            mongoTemplate.remove(query, YoboRecipe.class);
            return 0;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
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

    @PostMapping("/yobo/recipe/rate")
    public int rate(@RequestParam("Rid") String rid,@RequestParam("uid") String uid,@RequestParam("rate") double rate) {
        if(this.checkrate(rid,uid)==1) {
            Query query = new Query(where("_id").is(rid));
            YoboRecipe recipe =mongoTemplate.findOne(query,YoboRecipe.class);
            try {
                YoboRecipe.rated_people rated = new YoboRecipe.rated_people();
                rated.setUid(uid);
                rated.setRate(rate);
                mongoTemplate.updateFirst(query(where("_id").is(rid)), new Update().push("rated", rated), YoboRecipe.class);
                Update update =new Update();
                update.set("rating_count",recipe.getRating_count()+1);
                update.set("rating",(recipe.getRating_count()*recipe.getRating()+rate)/(recipe.getRating_count()+1));
                mongoTemplate.findAndModify(
                        query
                        ,update
                        ,YoboRecipe.class);
                return 1;
            } catch (Exception e) {
                System.out.println("Error");
                e.printStackTrace();
                return -1;
            }
        }else{
            return -1;
        }
    }

    @PostMapping("/yobo/recipe/checkrate")
    public int checkrate(@RequestParam("Rid") String rid,@RequestParam("uid") String uid){
        Query query = new Query();
        query.addCriteria(
                    new Criteria().andOperator(
                            where("_id").is(rid),
                            where("rated").elemMatch(where("uid").is(uid)))
            );
            YoboRecipe recipe=mongoTemplate.findOne(query,YoboRecipe.class);
            if(recipe==null){
                return 1;
            }else{
                return -1;
            }
    }

    @PostMapping(value = "/yobo/recipe/createRecipe", consumes = {"multipart/form-data"})
    public int createRecipe(@RequestParam("image")  List<MultipartFile> files,@RequestParam("recipe")  String recipe){
        System.out.println(("get message"));
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
        System.out.println(files.size());
        try {
            recipe1 = objectMapper.readValue(recipe, YoboRecipe.class);
            int imgcnt=0;
            for(int i=0;i<recipe1.getCooking_description().length;i++){
                if(recipe1.getCooking_description()[i].getImage().equals("NULL"))continue;
                else if(recipe1.getCooking_description()[i].getImage().equals("NULL")==false){
                    System.out.println(recipe1.getCooking_description()[i].getImage());
                    System.out.println(files.get(imgcnt));
                    String originFilename = files.get(imgcnt).getOriginalFilename();
                    String extName
                            = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
                    Long size = files.get(imgcnt).getSize();
                    // 서버에서 저장 할 파일 이름
                    String saveFileName = genSaveFileName(extName);
                    System.out.println("originFilename : " + originFilename);
                    System.out.println("extensionName : " + extName);
                    System.out.println("size : " + size);
                    System.out.println("saveFileName : " + saveFileName);
                    String tempfile=new String();
                    tempfile=saveFileName+extName;
                    File target = new File(SAVE_PATH, tempfile);

                    if (target.exists()){
                        System.out.println("파일이 중복됨");

                        UUID uuid = UUID.randomUUID();
                        System.out.println(uuid);
                        String convertPw = UUID.randomUUID().toString().replace("-", "");
                        saveFileName+=convertPw+extName;
                    }else{
                        saveFileName+=extName;
                    }
                    writeFile(files.get(imgcnt), saveFileName);
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


    @PostMapping(value = "/yobo/recipe/updateRecipe", consumes = {"multipart/form-data"})
    public int updateRecipe(@RequestParam("image")  List<MultipartFile> files,@RequestParam("recipe")  String recipe){
        System.out.println(("get message"));
        ObjectMapper objectMapper =new ObjectMapper();
        YoboRecipe recipe1;
        YoboRecipe recipe2;

        String url = null;
        int test[] ={0,1};
        System.out.println(test);
        System.out.println(files);
        //경로 확인 용
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        System.out.println(files.size());
        try {
            recipe1 = objectMapper.readValue(recipe, YoboRecipe.class);
            recipe2=this.getListbyDid(recipe1.get_id());
            int imgcnt = 0;
            for (int i = 0; i < recipe1.getCooking_description().length; i++) {
                if (recipe1.getCooking_description()[i].getImage().equals("NULL")||recipe1.getCooking_description()[i].getImage().equals("exit")) continue;
                else if (recipe1.getCooking_description()[i].getImage().equals("change")) {
                    System.out.println(recipe1.getCooking_description()[i].getImage());
                    System.out.println(files.get(imgcnt));
                    String originFilename = files.get(imgcnt).getOriginalFilename();
                    String extName
                            = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
                    Long size = files.get(imgcnt).getSize();
                    // 서버에서 저장 할 파일 이름
                    String saveFileName = genSaveFileName(extName);
                    System.out.println("originFilename : " + originFilename);
                    System.out.println("extensionName : " + extName);
                    System.out.println("size : " + size);
                    System.out.println("saveFileName : " + saveFileName);
                    String tempfile = new String();
                    tempfile = saveFileName + extName;
                    File target = new File(SAVE_PATH, tempfile);

                    if (target.exists()) {
                        System.out.println("파일이 중복됨");

                        UUID uuid = UUID.randomUUID();
                        System.out.println(uuid);
                        String convertPw = UUID.randomUUID().toString().replace("-", "");
                        saveFileName += convertPw + extName;
                    } else {
                        saveFileName += extName;
                    }
                    writeFile(files.get(imgcnt), saveFileName);
                    url = PREFIX_URL + saveFileName;
                    recipe1.getCooking_description()[i].setImage(url);
                    imgcnt += 1;
                }

            }
            for (int i = 0; i < recipe1.getCooking_description().length; i++) {
                if(recipe1.getCooking_description()[i].getImage().equals("exit")){
                    recipe1.getCooking_description()[i].setImage(recipe2.getCooking_description()[i].getImage());
                }
            }
            this.DeleteDid(recipe1.get_id());
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

        return fileName;
    }

    @GetMapping(value ="yobo/recipe/getByshortcut")
    public List<simpleRecipe> getByshortcut(@RequestParam("uid") String uid){
        Query query = query(where("_id").is(uid));
        YoboUser user=mongoTemplate.findOne(query, YoboUser.class);
        List<simpleRecipe> simple =new ArrayList();
        for(String Did: user.getRecipe_shotcut()){
            System.out.println(Did);
            simple.add(this.getSRecipebyDid(Did));
        }
        return simple;
    }

    @GetMapping(value = "/yobo/recipe/getByingredients")
    public List<simpleRecipe> getByingredients(@RequestParam("ingredients")  List<String> ingredients, @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        System.out.println(ingredients);

        List<Criteria> criterias = new ArrayList<Criteria>();
        for(String i:ingredients){
            criterias.add(new Criteria().where("main_cooking_ingredients").elemMatch(where("ingredients_name").is(i)));
            System.out.println(i);
        }
        Criteria criteria = new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]));
        Query query=new Query(criteria);

        query.limit(pageSize);
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, simpleRecipe.class);
    }
    @GetMapping("/yobo/recipe/getByrecommend")
    public List<simpleRecipe> getByrecommend(@RequestParam("favorite") List<String> favorite){
        List<simpleRecipe> sim = new ArrayList();
        int flag=0;
        for(String rec:favorite){
            List<simpleRecipe> tmp =new ArrayList();
            tmp=this.getListbyCate(rec,0,10);
            for(simpleRecipe t:tmp){
                flag=0;
                for(simpleRecipe s:sim){
                    if(s.get_id().equals(t.get_id())){
                        flag=1;
                    }
                }
                if(flag==0){
                    sim.add(t);
                    flag=0;
                    break;
                }
            }
        }
        System.out.println(sim);
        List<simpleRecipe> tmp2 =new ArrayList();
        if(favorite.size()==0){
            System.out.println(favorite.size());
            tmp2=this.getRecipeList(0,3);
            for(simpleRecipe t:tmp2){
                sim.add(t);
            }
        }else{
            tmp2=this.getListbyCate(favorite.get(0),0,10);
            int count=0;
            while(sim.size()<3){
                count+=1;
                if(count>10){
                    break;
                }
                System.out.println(sim.size());
                for(simpleRecipe t:tmp2){
                    if(sim.size()>=3){
                        break;
                    }
                    flag=0;
                    for(simpleRecipe s:sim){
                        if(s.get_id().equals(t.get_id())){
                            flag=1;
                        }
                    }
                    if(flag==0){
                        sim.add(t);
                        flag=0;
                    }
                }
            }

        }

        System.out.println(sim.size());
        return sim;
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


    @PostMapping(value = "/yobo/recipe/testimage", consumes = {"multipart/form-data"})
    public int testimage(@RequestParam("image")  List<MultipartFile> files){

        String url = null;
        int test[] ={0,1};
        System.out.println(test);
        System.out.println(files);
        //경로 확인 용
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        System.out.println(files.size());
        try {
              for(MultipartFile file:files) {
                  String originFilename = file.getOriginalFilename();
                  String extName
                          = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
                  Long size = file.getSize();
                  // 서버에서 저장 할 파일 이름
                  String saveFileName = genSaveFileName(extName);
                  System.out.println("originFilename : " + originFilename);
                  System.out.println("extensionName : " + extName);
                  System.out.println("size : " + size);
                  System.out.println("saveFileName : " + saveFileName);
//                  File target = new File(SAVE_PATH, saveFileName);
//                  if (target.exists()){
//                      System.out.println("파일이 중복됨");
//                      UUID uuid = UUID.randomUUID();
//                      System.out.println(uuid);
//                      String convertPw = UUID.randomUUID().toString().replace("-", "");
//                      saveFileName+=convertPw;
//                  }
                  writeFile(file, saveFileName);
                  url = PREFIX_URL + saveFileName;
              }
        }catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
        return 1;

    }

    @PostMapping(value = "/yobo/recipe/testimage2", consumes = {"multipart/form-data"})
    public int testimage2(@RequestParam("image")  MultipartFile[] files){

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

            for(MultipartFile file:files) {
                String originFilename = file.getOriginalFilename();
                String extName
                        = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
                Long size = file.getSize();
                // 서버에서 저장 할 파일 이름
                String saveFileName = genSaveFileName(extName);
                System.out.println("originFilename : " + originFilename);
                System.out.println("extensionName : " + extName);
                System.out.println("size : " + size);
                System.out.println("saveFileName : " + saveFileName);
                writeFile(file, saveFileName);
                url = PREFIX_URL + saveFileName;
            }
        }catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
        return 1;

    }
}
