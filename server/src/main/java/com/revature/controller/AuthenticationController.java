package com.revature.controller;

import com.revature.dto.LoginDTO;
import com.revature.dto.UserResponseDTO;
import com.revature.model.User;
import com.revature.service.JWTService;
import com.revature.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpSession;

public class AuthenticationController implements Controller {

    private UserService userService;
    private JWTService jwtService;

    public AuthenticationController() {
        this.userService = new UserService();
        this.jwtService = new JWTService();
    }

    private Handler login = ctx -> {

        LoginDTO loginInfo = ctx.bodyAsClass(LoginDTO.class);

        User user = userService.login(loginInfo.getUsername(), loginInfo.getPassword());

        String jwt = this.jwtService.createJWT(user);

        ctx.header("Access-Control-Expose-Headers", "*");
        ctx.header("Token", jwt);

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserRole());
        ctx.json(userResponseDTO);
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/login", login);
    }
}