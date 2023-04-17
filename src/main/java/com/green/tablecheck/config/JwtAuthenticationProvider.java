package com.green.tablecheck.config;

import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.domain.type.UserType;
import com.green.tablecheck.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class JwtAuthenticationProvider {
    private final String secretKey = "secretKey";
    private final String TOKEN_NAME = "X-AUTH-TOKEN";
    private final long TOKEN_VALID_TIME = 1000 * 60 *60 * 24;  // 하루

    // 토큰 생성
    public String createToken(String email, Long userId, UserType userType) {
        Claims claims = Jwts.claims()
            .setSubject(Aes256Util.encrypt(email))
            .setId(Aes256Util.encrypt(userId.toString()));
        claims.put("roles", userType);
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 재발행
    public String refreshToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);

        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    // 토큰으로부터 사용자 정보 가져오기
    public UserVo getUserVo(String token) {
        Claims c = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        return new UserVo(
            Long.valueOf(Objects.requireNonNull(Aes256Util.decrypted(c.getId())))  // id
            , Aes256Util.decrypted(c.getSubject()));  // email
    }
}