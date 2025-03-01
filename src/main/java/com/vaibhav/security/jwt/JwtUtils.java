package com.vaibhav.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;



@Component
@Slf4j
public class JwtUtils {

    /*
    * In this method we are doing couple of things
    * First we are getting the token from the request header
    * Second we are generating token from username
    * We are getting username from token
    * */

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private String jwtExpirationMs;

    //Method to get JwtHeader
    public String getJwtHeaders(HttpServletRequest request){
        log.info("Getting JWT Headers");
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken!=null && bearerToken.startsWith("Bearer")){
            log.info("Getting the token out of Bearer Token");
            return bearerToken.substring(7);
        }

        return null;
    }

    //Generate token from username
    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+Long.parseLong(jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    //Get Username from token
    public String getUserNameFromToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key()).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String token){
        try{
            System.out.println("Validating token");
            Jwts.parser()
                    .verifyWith((SecretKey) key()).build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
