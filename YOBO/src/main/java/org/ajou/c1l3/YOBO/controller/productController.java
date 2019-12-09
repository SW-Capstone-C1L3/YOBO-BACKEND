package org.ajou.c1l3.YOBO.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ajou.c1l3.YOBO.domain.YoboProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
@CrossOrigin
@RestController
public class productController {
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();

    private  String SAVE_PATH = s+"/Product/upload";
    private  String PREFIX_URL = s+"/Product/upload/";
    //test
    @Autowired
    MongoTemplate mongoTemplate;


    @PostMapping(value = "/yobo/product/createProduct", consumes = {"multipart/form-data"})
    public int createProduct(@RequestParam("img")  MultipartFile files,@RequestParam("product_name") String product_name,@RequestParam("product_price")
            int product_price,@RequestParam("product_category") String product_category,@RequestParam("product_qty") int product_qty,@RequestParam("product_unit")
            String product_unit,@RequestParam("product_description") String product_description,@RequestParam("provided_company_id") String provided_company_id,@RequestParam("company_name")
            String company_name ){
            YoboProduct product1=new YoboProduct();
        String url = null;
        //경로 확인 용
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        product1.setProduct_name(product_name);
        product1.setCompany_name(company_name);
        product1.setProduct_price(product_price);
        product1.setProduct_category(product_category);
        product1.setProduct_qty(product_qty);
        product1.setProduct_unit(product_unit);
        product1.setProduct_description(product_description);
        product1.setProvided_company_id(provided_company_id);
        product1.setCompany_name(company_name);
        try {
            int imgcnt=0;
                    String originFilename = files.getOriginalFilename();
                    String extName
                            = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
                    System.out.println(originFilename);
                    Long size = files.getSize();
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
                    writeFile(files, saveFileName);
                    url = PREFIX_URL + saveFileName;
                    product1.setProduct_image(url);
                    imgcnt+=1;

            mongoTemplate.insert(product1);

        }catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
        return 1;

    }

    @RequestMapping(value = "/yobo/product/getImage/",method= RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestParam String filePath) throws IOException {
        RandomAccessFile f = new RandomAccessFile(filePath, "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
    }

    @GetMapping("/yobo/product/search/")
    public List<YoboProduct> getProduct(@RequestParam("productName") String productName , @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("product_name").regex(productName));
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, YoboProduct.class);
    }

    @PostMapping("/yobo/product/delete/")
    public int deleteProduct(@RequestParam("Did") String Did){
        Query query = Query.query(where("_id").is(Did));
        try {
            mongoTemplate.remove(query, YoboProduct.class);

        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @GetMapping("/yobo/product/searchbyPid/")
    public List<YoboProduct> getProductbyPID(@RequestParam("PID") String PID , @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        System.out.println(PID);
        Query query = Query.query(where("provided_company_id").is(PID));
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, YoboProduct.class);
    }

    @GetMapping("/yobo/product/searchbyDid/")
    public YoboProduct getProductbyDID(@RequestParam("Did") String Did , @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("_id").is(Did));
        query.skip(pageNum*pageSize);
        return mongoTemplate.findOne(query, YoboProduct.class);
    }

    @GetMapping("/yobo/product/getProducteList/")
    public  List<YoboProduct> getProducteList(@RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum,@RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        int skipn=pageNum*pageSize;
        SkipOperation skip= Aggregation.skip(skipn);
        Aggregation aggregation = Aggregation.newAggregation(skip);
        AggregationResults<YoboProduct> results = mongoTemplate.aggregate(aggregation,"Product",YoboProduct.class);
        List<YoboProduct> list = results.getMappedResults();  //결과
        return list;
    }

    @GetMapping("/yobo/product/searchbycate/")
    public List<YoboProduct> getProductbycate(@RequestParam("cate") String cate , @RequestParam(value="pageNum",required = false,defaultValue = "0")int pageNum, @RequestParam(value="pageSize",required = false,defaultValue = "10") int pageSize){
        Query query = Query.query(where("product_category").regex(cate));
        query.skip(pageNum*pageSize);
        return mongoTemplate.find(query, YoboProduct.class);
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
