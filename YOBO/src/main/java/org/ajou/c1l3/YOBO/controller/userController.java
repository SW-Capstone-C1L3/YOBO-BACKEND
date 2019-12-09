package org.ajou.c1l3.YOBO.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ajou.c1l3.YOBO.domain.YoboBasket;
import org.ajou.c1l3.YOBO.domain.YoboRecipe;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.ajou.c1l3.YOBO.repository.CurrentRecipeRepository;
import org.bson.types.ObjectId;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;

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
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class userController {


    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();

    private  String SAVE_PATH = s+"/User/upload";
    private  String PREFIX_URL = s+"/User/upload/";

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/yobo/user/getbyEmail")
    public YoboUser getUserInfo(@RequestParam("Emai") String user_email,@RequestParam("user_email") String user_name){
        Query query = query(where("user_email").is(user_email));
        System.out.println("이메일"+user_email);
        YoboUser user=mongoTemplate.findOne(query, YoboUser.class);
        if(user==null){
            YoboUser tmpUser=new YoboUser();
            String[] array = user_email.split("@");
            tmpUser.setUser_email(user_email);
            tmpUser.setUser_name(user_name);
            tmpUser.setUser_name(array[0]);
            createUser(tmpUser);
        }
        user=mongoTemplate.findOne(query, YoboUser.class);
        System.out.println(user);
        return user;
    }


    @GetMapping("/yobo/user/getbyDid")
    public YoboUser getUserbyDid(@RequestParam("Did") String Did){
        System.out.println(Did);
        Query query = query(where("_id").is(Did));

        YoboUser user=mongoTemplate.findOne(query, YoboUser.class);
        System.out.println(Did);
        System.out.println(user);
        return user;
    }

    @PostMapping("/yobo/recipe/createUser")
    public YoboUser createUser(@RequestBody YoboUser user){
        basketController basketController = new basketController(); //User의 장바구니 생성- 회원가입시 미리 생성
        basketController.createBasket(user.getUser_id());
        mongoTemplate.insert(user);
        return user;
    }

    @PostMapping(value="/yobo/recipe/updateUser",consumes = {"multipart/form-data"})
    public int updateUser(@RequestParam(value="image",required = false) MultipartFile file, @RequestParam("user") String user){
        ObjectMapper objectMapper =new ObjectMapper();
        YoboUser tmpuser;
        String url = null;

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        try {
            tmpuser = objectMapper.readValue(user, YoboUser.class);
            YoboUser tmpuser2=mongoTemplate.findOne(new Query(where("_id").is(tmpuser.get_id())),YoboUser.class);
            System.out.println(tmpuser.getImage());
                if(tmpuser.getImage().equals("change")) {
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
                    writeFile(file, saveFileName);
                    url = PREFIX_URL + saveFileName;
                    tmpuser.setImage(url);
                }else if(tmpuser.getImage().equals("exit")){
                    tmpuser.setImage(tmpuser2.getImage());
                }
                    Query query=query(where("_id").is(tmpuser.get_id()));
                    Update update =new Update();
                    update.set("user_id",tmpuser.getUser_id());
                    update.set("user_name",tmpuser.getUser_name());
                    update.set("user_age",tmpuser.getUser_age());
                    update.set("interest_category",tmpuser.getInterest_category());
                    update.set("user_grade",tmpuser.getUser_grade());
                    update.set("user_email",tmpuser.getUser_email());
                    update.set("user_address",tmpuser.getUser_address());
                    update.set("recipe_shotcut",tmpuser.getRecipe_shotcut());
                    update.set("image",tmpuser.getImage());
                    mongoTemplate.findAndModify(
                    query
                    ,update
                    ,YoboUser.class);

        }catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }

        return 1;
    }

    @PostMapping("/yobo/recipe/addShortCut")
    public int addShortCut(@RequestParam("Uid") String Uid, @RequestParam("Rid") String Rid){
        try {
            Query query = new Query();
            query.addCriteria(
                    new Criteria().andOperator(
                            where("_id").is(Uid),
                            where("recipe_shotcut").in(Rid))
            );
//            Query query = new Query("{'elements' : 'ele_1'}");
            if(mongoTemplate.findOne(query,YoboUser.class)!=null){ //이미 존재하는 즐겨찾기인지 확인, 존재한다면 삭제후 2반환.
                System.out.println("exit");
                this.DeleteShortCut(Uid,Rid);
                return 2;
            }else {
                mongoTemplate.updateFirst(query(where("_id").is(Uid)), new Update().push("recipe_shotcut", Rid), YoboUser.class);
                return 1;
            }

        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping("/yobo/recipe/DeleteShortCut")
    public int DeleteShortCut(@RequestParam("Uid") String Uid, @RequestParam("Rid") String Rid){
        try {
            Query query = query( where("_id").is(Uid));
            Update update = new Update().pull("recipe_shotcut", Rid);
            mongoTemplate.updateMulti(query, update, YoboUser.class);
            return 1;
        }catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            return -1;
        }
    }
@RequestMapping(value = "/yobo/user/getImage/",method= RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@RequestParam String filePath) throws IOException {
        RandomAccessFile f = new RandomAccessFile(filePath, "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
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
