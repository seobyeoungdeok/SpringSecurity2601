package com.example.demo.config.auth;

import com.example.demo.dao.MemberDao;
import com.example.demo.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// 이 클래스는 언제 동작되냐면
// 시큐리티 설정에서 loginProcessingUrl("/login")
//-> /login요청이 오면 자동으로 UserDetailsService타입으로 IoC되어 있는 loadUserByUsername가
//실행 됨
// 시큐리티 session내부에 Authentication내부에 UserDetails들어감
@Log4j2
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberDao memberDao;
/*
    public PrincipalDetailsService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername username={}", username);
        // User타입은 Authentication에 직접 담을 수 없음.
        // Authentication에 담을 수 있는 타입은 오직 UserDetails타입만 가능함.
        //select * from member202601 where username=?
        User user = memberDao.login(username);
        log.info(user.getRole());
        log.info(user.toString());//오라클 서버에서 가져온 값
        if(user !=null){//DB에서 찾아온 정보를 들고 있으면
            //Authentication에 담을 수 있는 타입으로 변경해줘
            return new PrincipalDetails(user);
        }
        return null;
    }
}
