package org.ajou.c1l3.YOBO.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ajou.c1l3.YOBO.domain.YoboCompany;
import org.ajou.c1l3.YOBO.domain.YoboUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles requests for the application home page.
 */
@RestController
public class LoginController {

    /* NaverLoginBO */
    private NaverLoginBO naverLoginBO;
    private String apiResult = null;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    userController userController;
    @Autowired
    companyController companyController;
    @Autowired
    private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
        this.naverLoginBO = naverLoginBO;
    }

    //로그인 첫 화면 요청 메소드
    @RequestMapping(value = "/users/naverlogin", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(Model model, HttpSession session) {

        /* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
        String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);

        //https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=sE***************&
        //redirect_uri=http%3A%2F%2F211.63.89.90%3A8090%2Flogin_project%2Fcallback&state=e68c269c-5ba9-4c31-85da-54c16c658125
        System.out.println("네이버:" + naverAuthUrl);

        //네이버
        model.addAttribute("url", naverAuthUrl);

        /* 생성한 인증 URL을 View로 전달 */
        return "users/naverlogin";
    }

    @GetMapping("/users/naverlog")
    public YoboUser getId(@RequestParam("at") OAuth2AccessToken at) throws IOException {
        apiResult = naverLoginBO.getUserProfile(at);
        System.out.println(apiResult);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(apiResult);
        String userEmail=jsonElement.getAsJsonObject().get("response").getAsJsonObject().get("email").toString();
        String username=jsonElement.getAsJsonObject().get("response").getAsJsonObject().get("name").toString();
        System.out.println(userEmail);
        userEmail=userEmail.replaceAll("\"", "");
        System.out.println("11");
//        Query query = query(where("user_email").is(userEmail));
//        System.out.println("이메일"+userEmail);
//        YoboUser user=mongoTemplate.findOne(query, YoboUser.class);
        System.out.println("11");
        return  userController.getUserInfo(userEmail,username);
//        return  user;
    }

    @CrossOrigin
    @GetMapping("/company/naverlog")
    public YoboCompany companyLogin(@RequestParam("email") String email) throws IOException {

         if(companyController.getCompanyByEmail(email)==null){
             YoboCompany cmp=new YoboCompany();
             cmp.setCompany_ld(email);
              companyController.createCompany(cmp);
             return companyController.getCompanyByEmail(email);
         }else{
             return companyController.getCompanyByEmail(email);
         }
    }
    //네이버 로그인 성공시 callback호출 메소드
    @RequestMapping(value = "/users/callback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session)
            throws IOException {
        System.out.println("여기는 callback");
        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginBO.getAccessToken(session, code, state);
        //로그인 사용자 정보를 읽어온다.
        apiResult = naverLoginBO.getUserProfile(oauthToken);
        System.out.println(naverLoginBO.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        System.out.println("result"+apiResult);
        /* 네이버 로그인 성공 페이지 View 호출 */
//      JSONObject jsonobj = jsonparse.stringToJson(apiResult, "response");
//      String snsId = jsonparse.JsonToString(jsonobj, "id");
//      String name = jsonparse.JsonToString(jsonobj, "name");
//
//      UserVO vo = new UserVO();
//      vo.setUser_snsId(snsId);
//      vo.setUser_name(name);
//
//      System.out.println(name);
//      try {
//          vo = service.naverLogin(vo);
//      } catch (Exception e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }


//      session.setAttribute("login",vo);
//      return new ModelAndView("user/loginPost", "result", vo);

        return apiResult.toString();
    }
}
