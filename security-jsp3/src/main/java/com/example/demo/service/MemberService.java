package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;
    public void memberInsert(User user) {
        memberDao.memberInsert(user);
    }//end of memberInsert
}
