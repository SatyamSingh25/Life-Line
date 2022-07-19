package com.hu22.bloodBankBackendPrivate.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "create key is this";

    private static final int TOKEN_VALIDITY = 3600 *5;

    public String getEmailFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    //called as higher order function which takes or returns functions
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    //
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); //define the secret key
    }


    //validating token
    public boolean validateToken(String token, UserDetails userDetails){
         String userName = getEmailFromToken(token);
         return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //checking that is this token is expired or not
    private boolean isTokenExpired(String token){
        final Date expirationDate = getExirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //generating the token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) //setting up the subject with unique value/string
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

}
