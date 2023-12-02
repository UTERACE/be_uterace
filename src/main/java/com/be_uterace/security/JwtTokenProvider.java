package com.be_uterace.security;

import com.be_uterace.exception.APIException;
import com.be_uterace.exception.ErrorHolder;
import com.be_uterace.exception.JWTException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-access-token-expiration-milliseconds}")
    private long jwtAccessTokenExpirationDate;

    @Value("${app-jwt-refresh-token-expiration-milliseconds}")
    private long jwtRefreshTokenExpirationDate;

    // generate JWT token
    public String generateAccessToken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtAccessTokenExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    public String generateAccessToken(String username){

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtAccessTokenExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    public String generateRefreshToken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtRefreshTokenExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    public String generateRefreshToken(String username){
        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtRefreshTokenExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // get username from Jwt token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    // validate Jwt token
    public boolean validateToken(String token) {
        if (token.isEmpty())
            throw new JWTException("JWT claims string is empty");
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (JwtException e) {
            if (e instanceof MalformedJwtException) {
                ErrorHolder.setErrorMessage("Invalid Token");
                throw new JWTException("Invalid Token");
            } else if (e instanceof ExpiredJwtException) {
                ErrorHolder.setErrorMessage("JWT token is expired");
                throw new JWTException("Token is expired");
            } else if (e instanceof UnsupportedJwtException) {
                throw new JWTException("JWT token is unsupported");
            } else {
                ErrorHolder.setErrorMessage("JWT claims string is empty");
                throw new JWTException("JWT token is unsupported");
            }


        }
    }
}
