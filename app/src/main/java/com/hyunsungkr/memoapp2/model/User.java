package com.hyunsungkr.memoapp2.model;

import java.io.Serializable;


// 레트로핏 라이브러리를 통해서 Body에 Json으로 데이터를 보낼 클래스
// 1. Serialzible해준다.
// 2. API 명세서를 보고 멤버 변수를 만든다.
// 3. 멤버 변수의 접근제어자를 private으로 해준다.
// 4. Getter / Setter 만들어준다.
public class User implements Serializable {

    private String email;
    private String password;
    private String nickname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    public User(){
    }
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
