package com.revature.controller;

import com.revature.exception.EmployeetNotFoundException;
import com.revature.exception.InvalidImageException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.exception.UploadFailedException;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.FailedLoginException;

public class ExceptionController implements Controller {
    private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private ExceptionHandler<FailedLoginException> failedLogin = (exception, ctx) -> {
        logger.warn("User login failed. Exception message is " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    };

    private ExceptionHandler<IllegalArgumentException> invalidArgument = (exception, ctx) -> {
        logger.warn("Invalid Argument. Exception message is " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    };

    private ExceptionHandler<ReimbursementNotFoundException> reimbursementNotFound = (exception, ctx) -> {
        logger.warn("Reimbursement not found. Exception message is " + exception.getMessage());
        ctx.status(404);
        ctx.json(exception.getMessage());
    };

    private ExceptionHandler<EmployeetNotFoundException> employeeNotFound = (exception, ctx) -> {
        logger.warn("Employee not found. Exception message is " + exception.getMessage());
        ctx.status(404);
        ctx.json(exception.getMessage());
    };

    private ExceptionHandler<InvalidImageException> invalidImage = (exception, ctx) -> {
        logger.warn("Invalid image type. Exception message is " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    };

    private ExceptionHandler<UploadFailedException> uploadFailed = (exception, ctx) -> {
        logger.warn("Fail to upload image. Exception message is " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());


    };
    @Override
    public void mapEndpoints(Javalin app) {
        app.exception(FailedLoginException.class, failedLogin);
        app.exception(IllegalArgumentException.class, invalidArgument);
        app.exception(ReimbursementNotFoundException.class, reimbursementNotFound);
        app.exception(EmployeetNotFoundException.class, employeeNotFound);
        app.exception(InvalidImageException.class, invalidImage);
        app.exception(UploadFailedException.class, uploadFailed);
    }
}
