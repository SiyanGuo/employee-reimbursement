package com.revature.main;

import com.revature.controller.AuthenticationController;
import com.revature.controller.Controller;
import com.revature.controller.ExceptionController;
import com.revature.controller.ReimbursementController;
import io.javalin.Javalin;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Driver {

    public static Logger logger = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {

        Javalin app = Javalin.create((config) -> {
            config.enableCorsForAllOrigins();
        });
        app.before((ctx) -> {
            logger.info(ctx.method() + " request received for " + ctx.path());
        });
        mapControllers(app, new AuthenticationController(), new ReimbursementController(), new ExceptionController());

        app.start(8081);
    }

    public static void mapControllers(Javalin app, Controller... controllers) {
        for (Controller c : controllers) {
            c.mapEndpoints(app);
        }
    }
}

//    String salt = BCrypt.gensalt();
//    String hashedPassword = BCrypt.hashpw("pass123", salt);
//        System.out.println(hashedPassword);
//
//                if (BCrypt.checkpw("pass123", hashedPassword)) {
//                System.out.println("matched");
//                } else {
//                System.out.println("not matched");
//                }