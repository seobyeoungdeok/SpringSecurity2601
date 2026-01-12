package com.example.demo.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;//ROLE_USER,ROLE_MANAGER ,ROLE_ADMIN
    private String createDate;
    //소셜 로그인 - 카카오
    private String provider;//google, kakao, naver
    private String providerId; //uid, 카카오식별자, 네이버 식별자
}
