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
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        LoginDTO loginInfo = new LoginDTO();
        loginInfo.setPassword(password);
        loginInfo.setUsername(username);

       User user = userService.login(loginInfo.getUsername(), loginInfo.getPassword());

        String jwt = this.jwtService.createJWT(user);

        ctx.header("Access-Control-Expose-Headers", "*");
        ctx.header("Token", jwt);

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserRole());
        ctx.json(userResponseDTO);
    };

    private Handler signup = ctx -> {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email");
        String role = ctx.formParam("userRole");

        User signedUpUser = new User(-1, username, password, firstName,lastName,email, role);
  //      User signedUpUser = ctx.bodyAsClass(User.class);

        User user = userService.signUp(signedUpUser);
        String jwt = this.jwtService.createJWT(user);

        ctx.header("Access-Control-Expose-Headers", "*");
        ctx.header("Token", jwt);
        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserRole());
        ctx.json(userResponseDTO);
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/login", login);
        app.post("/signup",signup);
    }
}