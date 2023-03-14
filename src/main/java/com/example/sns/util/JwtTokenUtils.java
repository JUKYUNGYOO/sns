package com.example.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parserBuilder;

//token parsing
public class JwtTokenUtils {
    //토큰에서 userName을 가지고옴..
    public static String getUserName(String token, String key){
        return extractClaims(token,key).get("userName",String.class);


    }
    public static boolean isExpired(String token, String key){
        Date expiredDate = extractClaims(token,key).getExpiration();
        return expiredDate.before(new Date());

    }
    //jwt token parsing - claims을 가지고 옴..

    //parserBuilder에 key를 넣어줌.
    private static Claims extractClaims(String token, String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();  //토큰을 가지고 와서, 파싱해줌
        //parseClaimsJwt -> parseClaimsJws
        //error occurs while validation.io.jsonwebtoken.UnsupportedJwtException: Signed Claims JWSs are not supported.
    }
    public static String generateToken(String userName,String key,long expiredTimeMs){
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }
    private static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
