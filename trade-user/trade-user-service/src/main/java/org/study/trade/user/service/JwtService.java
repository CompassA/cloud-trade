package org.study.trade.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.study.trade.user.model.UserDTO;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Service
public class JwtService implements EnvironmentAware {

    private static final Duration VALID_DURATION = Duration.ofDays(2);
    private static final String CLAIM_USER_ID = "user_id";
    private static final String CLAIM_USER_NICK_NAME = "user_nick_name";
    private static final String CLAIM_USER_GENDER = "user_gender";
    private static final String CLAIM_USER_TELEPHONE = "user_telephone";

    private SecretKey secretKey;

    public String generateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userDTO.getId());
        claims.put(CLAIM_USER_NICK_NAME, userDTO.getNick());
        claims.put(CLAIM_USER_GENDER, userDTO.getGender());
        claims.put(CLAIM_USER_TELEPHONE, userDTO.getTelephone());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDTO.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + VALID_DURATION.toMillis()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean isExpired(Claims jwt) {
        return new Date(System.currentTimeMillis())
                .after(jwt.getExpiration());
    }

    public Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public UserDTO getUserInfo(String jwt) {
        Claims claims = parseJWT(jwt);
        if (isExpired(claims)) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(claims.get(CLAIM_USER_ID, Long.class));
        userDTO.setNick(claims.get(CLAIM_USER_NICK_NAME, String.class));
        userDTO.setGender(claims.get(CLAIM_USER_GENDER, Byte.class));
        userDTO.setTelephone(claims.get(CLAIM_USER_TELEPHONE, Long.class));
        return userDTO;

    }

    @Override
    public void setEnvironment(Environment environment) {
        String property = environment.getProperty("jwt.secret");
        if (StringUtils.isBlank(property)) {
            throw new IllegalArgumentException("no jwt secret");
        }
        byte[] secret = property.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(secret, 0, secret.length, "AES");
    }
}
