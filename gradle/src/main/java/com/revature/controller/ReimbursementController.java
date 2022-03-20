package com.revature.controller;

import com.revature.model.Reimbursement;
import com.revature.service.JWTService;
import com.revature.service.ReimbursementService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;


public class ReimbursementController implements Controller{

    private JWTService jwtService;
    private ReimbursementService reimbursementService;


    public ReimbursementController() {
        this.jwtService = new JWTService();
        this.reimbursementService = new ReimbursementService();
    }

    private Handler getAllReimbursements = (ctx) ->{
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);
        if (!token.getBody().get("user_role").equals("Finance Manager")) {
            throw new UnauthorizedResponse("You must be a Finance Manager to access this endpoint");
        }

        List<Reimbursement> assignments = this.reimbursementService.getAllReimbursements();

        ctx.json(assignments);
    };



    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/api/reimbursements", getAllReimbursements); //manager only
      //  app.patch("/api/reimbursements/{reimbursement_id}",approveOrDenyReimbursement); //manager only
      // app.get("/api/users/{user_id}/reimbursements",getSpecificEmployeeReimbursements); //employee
      //  app.post("/api/users/{user_id}/reimbursements", addReimbursement); //employee
    }
}
