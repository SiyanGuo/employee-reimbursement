package com.revature.service;

import com.revature.model.User;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    public void setup(){
        jwtService   = new JWTService();
    }

//    positive
    @Test
    public void test_createJWT(){
        User fakeUser = new User (10,"employee","password","Nana","Lu", "nl@gmail.com","EMPLOYEE");
        String jwt = jwtService.createJWT(fakeUser);
        Assertions.assertNotNull(jwt);
    }

//    negative
    @Test
    public void test_parseJwt(){

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW5hZ2VyMTExIiwidXNlcl9pZCI6MiwidXNlcl9yb2xlIjoiRklOQU5DRSBNQU5BR0VSIn0.dXolj87yLWWKJ_BWWrFxptXwpfRUatzD3xNZ-xocxni";
        Assertions.assertThrows(UnauthorizedResponse.class, ()->{
            jwtService.parseJwt(jwt);
        });
    }

//    negative
    @Test
    public void test_verifyEmployeeRole(){
        User fakeUser = new User (10,"employee","password","Nana","Lu", "nl@gmail.com","Employer");
        String jwt = jwtService.createJWT(fakeUser);
        Assertions.assertThrows(UnauthorizedResponse.class, ()->{
            jwtService.verifyEmployee(jwtService.parseJwt(jwt), "10");
        });

    }
//negative
    @Test
    public void test_verifyEmployeeId(){
        User fakeUser = new User (8,"employee","password","Nana","Lu", "nl@gmail.com","Employer");
        String jwt = jwtService.createJWT(fakeUser);
        Assertions.assertThrows(UnauthorizedResponse.class, ()->{
            jwtService.verifyEmployee(jwtService.parseJwt(jwt), "10");
        });
    }
//negative
    @Test
    public void test_verifyManagerRole(){
        User fakeUser = new User (9,"manager","password","Nana","Lu", "nl@gmail.com","Finance Manage");
        String jwt = jwtService.createJWT(fakeUser);
        Assertions.assertThrows(UnauthorizedResponse.class, ()->{
            jwtService.verifyManager(jwtService.parseJwt(jwt));
        });

    }


}


