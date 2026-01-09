package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
// 스포츠 센터
// 역할(ROLE - 인가) : user, manager, admin

@Log4j2
@Controller
@RequiredArgsConstructor//- 각 메서드가 view를 리턴한다.
public class indexController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //http://localhost:8000 or http://localhost:8000/
    @GetMapping({"","/"})
    public String index(){
        log.info("index");
        //-> /WEB-INF/views/{{index}}.jsp
        // 어노테이션이 RestController에서 Controller 변경됨
        // @RestController = @Controller + @ResponseBody -> 문자열 포맷
        // @Controller => 문자열이 출력으로 나갈 화면 이름이다.
        return "index";// -> ViewResolver
    }//end of home
    //http://localhost:8000/user
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
    //http://localhost:8000/admin
    @GetMapping({"","/admin"})
    public String admin(){
        log.info("admin");
        return "admin";
    }//end of home
    //로그인 화면 요청하기
    //http://localhost:8000/loginForm
    @GetMapping("/loginForm")
    public String loginForm(){
        log.info("loginForm");
        return "auth/loginForm";
    }//end of loginForm

    //회원가입 호출하기
    //http://localhost:8000/joinForm
    @GetMapping("/joinForm")
    public String joinForm(){
        log.info("joinForm");
        // auth/joinForm -> 응답페이지 화면 이름이다.
        // yaml -> /WEB-INF/views/ 접두어
        // 접미어     -> .jsp
        return "auth/joinForm";
    }
    //http://localhost:8000/login-error
    @GetMapping("/login-error")
    public String loginError(){
        log.info("login-error");
        return "loginError";
    }//end of loginError
    //회원가입 구현하기
    @PostMapping("/join")
    public String join(User user){
        log.info("join");
        user.setRole("ROLE_USER");
        // 패스워드 암호화하기
        String rawPassword =  user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        //비번 123으로 등록은 됨. 그러나 시큐리티 로그인 할 수 없음
        //왜냐면 암호화가 되지 않은 비번에 대해서는 처리 안됨
        user.setPassword(encPassword);
        memberService.memberInsert(user);
        return "auth/loginForm";
    }


}
