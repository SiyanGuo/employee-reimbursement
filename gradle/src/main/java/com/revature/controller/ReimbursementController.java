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

    private Handler resolveReimbursement = (ctx) ->{
        if(ctx.header("Authorization")==null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);
        if (!token.getBody().get("user_role").equals("Finance Manager")) {
            throw new UnauthorizedResponse("You must be a Finance Manager to access this endpoint");
        }
        int trainerId = token.getBody().get("user_id", Integer.class);
        String reimbursementId = ctx.pathParam("reimbursement_id");

        String status = ctx.formParam("status");
        System.out.println("status form param"+status);
        Reimbursement reimbursement = this.reimbursementService.resolveReimbursement(status, trainerId, reimbursementId);

        ctx.json(reimbursement);
    };

    private Handler getSpecificEmployeeReimbursements = (ctx) ->  {
        if(ctx.header("Authorization")==null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];
        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        if (!token.getBody().get("user_role").equals("EMPLOYEE")) {
            throw new UnauthorizedResponse("You must be an employee to access this endpoint");
        }

        String userId = ctx.pathParam("user_id");
        if (!token.getBody().get("user_id").toString().equals(userId)) {
            throw new UnauthorizedResponse("You cannot obtain assignments that don't belong to yourself");
        }

        List<Reimbursement> reimbursements = this.reimbursementService.getSpecificEmployeeReimbursements(userId);
        ctx.json(reimbursements);
    };


    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/reimbursements", getAllReimbursements); //manager only
        app.patch("/reimbursements/{reimbursement_id}",resolveReimbursement); //manager only
        app.get("/users/{user_id}/reimbursements",getSpecificEmployeeReimbursements); //employee only
      //  app.post("/api/users/{user_id}/reimbursements", addReimbursement); //employee only
    }
}
