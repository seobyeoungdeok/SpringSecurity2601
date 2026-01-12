package com.example.demo.dao;

import com.example.demo.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class MemberDao {
    private final SqlSessionTemplate sqlSessionTemplate;

    public void memberInsert(User user) {
        log.info("memberInsert");
        int result = -1;
        result = sqlSessionTemplate.insert("memberInsert", user);
        log.info(result);
    }//end of memberInsert

    public User login(String username) {
        log.info("login");
        User user = null;
        try {
            user = sqlSessionTemplate.selectOne("login", username);
            log.info(user.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }//end of login
}
