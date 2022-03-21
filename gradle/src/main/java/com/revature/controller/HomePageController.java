package com.revature.controller;

import com.revature.model.Reimbursement;
import com.revature.service.JWTService;
import com.revature.service.ReimbursementService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageController implements Controller{
    private JWTService jwtService;
    private ReimbursementService reimbursementService;


    public HomePageController() {
        this.jwtService = new JWTService();
        this.reimbursementService = new ReimbursementService();
    }
    private Handler getMyReimbursements = (ctx) ->{
//        Map<String, Object> model = new HashMap<>();
//        model.put("message", "Hello World");
//        model.put("now", LocalDateTime.now());
//        ctx.render("/templates/layout.html", model);

        Map<String, Object> model = new HashMap<>();
//        if(ctx.header("Authorization")==null) {
//            throw new UnauthorizedResponse("You must be logged in to access this endpoint");
//        }
//        String jwt = ctx.header("Authorization").split(" ")[1];
//
//        Jws<Claims> token = this.jwtService.parseJwt(jwt);
//
//        if (!token.getBody().get("user_role").equals("Finance Manager")) {
//            throw new UnauthorizedResponse("You must be a Finance Manager to access this endpoint");
//        }

        List<Reimbursement> reimbursements = this.reimbursementService.getAllReimbursements();
        model.put("reimbursements", reimbursements);
        ctx.render("/templates/layout.html", model);
    };

    private Handler homepage = (ctx) ->{
        ctx.result("hello world!");
    };
    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/", homepage);
        app.get("/my-reimbursements", getMyReimbursements);
    }
}
