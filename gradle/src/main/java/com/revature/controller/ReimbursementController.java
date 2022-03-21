package com.revature.controller;

import com.revature.model.Reimbursement;
import com.revature.service.JWTService;
import com.revature.service.ReimbursementService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ReimbursementController implements Controller{

    private JWTService jwtService;
    private ReimbursementService reimbursementService;


    public ReimbursementController() {
        this.jwtService = new JWTService();
        this.reimbursementService = new ReimbursementService();
    }

    private Handler getAllReimbursements = (ctx) ->{

        if(ctx.header("Authorization")==null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        if (!token.getBody().get("user_role").equals("Finance Manager")) {
            throw new UnauthorizedResponse("You must be a Finance Manager to access this endpoint");
        }

        List<Reimbursement> reimbursements = this.reimbursementService.getAllReimbursements();

        ctx.json(reimbursements);
    };




    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/api/reimbursements", getAllReimbursements); //manager only
      //  app.patch("/api/reimbursements/{reimbursement_id}",approveOrDenyReimbursement); //manager only
      // app.get("/api/users/{user_id}/reimbursements",getSpecificEmployeeReimbursements); //employee
      //  app.post("/api/users/{user_id}/reimbursements", addReimbursement); //employee
    }
}
