package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 스포츠 센터
// 역할(ROLE - 인가) : user, manager, admin

@Log4j2
@Controller  //- 각 메서드가 view를 리턴한다.
public class indexController {
    @GetMapping({"","/"})
    public String home(){
        log.info("home");
        //-> /WEB-INF/views/{{index}}.jsp
        return "index";// -> ViewResolver
    }//end of home
    @GetMapping({"","/user"})
    public String user(){
        log.info("home");
        return "home";
    }//end of home
    @GetMapping({"","/manager"})
    public String manager(){
        log.info("manager");
        return "manager";
    }//end of manager
    @GetMapping({"","/admin"})
    public String admin(){
        log.info("admin");
        return "admin";
    }//end of home
    //로그인 화면 요청하기
    @GetMapping("/loginForm")
    public String loginForm(){
        log.info("loginForm");
        return "auth/loginForm";
    }//end of loginForm

    //회원가입 호출하기
    @GetMapping("/joinForm")
    public String joinForm(){
        log.info("joinForm");
        // auth/joinForm -> 응답페이지 화면 이름이다.
        // yaml -> /WEB-INF/views/ 접두어
        // 접미어     -> .jsp
        return "auth/joinForm";
    }
    @GetMapping("/login-error")
    public String loginError(){
        log.info("login-error");
        return "login-error";
    }


}
