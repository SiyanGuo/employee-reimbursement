package com.revature.service;

import com.revature.model.User;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JWTService {

    private Key key;

    public JWTService() {
        byte[] secret = "7haG2GutwyIgaOKFDyGcqTraxiXF9sedJEG8kHOYIDc=".getBytes();
        key = Keys.hmacShaKeyFor(secret);
    }


    public String createJWT(User user) {
        String jwt = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getId())
                .claim("user_role", user.getUserRole())
                .signWith(key)
                .compact();

        return jwt;
    }


    public Jws<Claims> parseJwt(String jwt) {
        try {
            Jws<Claims> token = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return token;
        } catch(JwtException e) {
            throw new UnauthorizedResponse("JWT was invalid");
        }

    }

}
