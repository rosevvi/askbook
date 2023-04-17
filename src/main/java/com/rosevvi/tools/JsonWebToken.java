package com.rosevvi.tools;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: rosevvi
 * @date: 2023/3/23 16:32
 * @version: 1.0
 * @description:
 */
@Slf4j
public class JsonWebToken {

    public static final String SECRET_KEY = "rosevvi!@#$%";


    public String getJwt(Long id){
//        String sid = AESUtils.encrypt(id + "");
//        System.out.println(sid+"setjwt===");
        String key = JWT.create()
                .withClaim("id", id)
                .withExpiresAt(DateUtils.addYears(new Date(), 1))
                .sign(Algorithm.HMAC256(SECRET_KEY));
        return "Bearer "+key;
    }

    public Long getCalim(String token){
        token = token.split("Bearer")[1].trim();
        log.info(token+"!!!!!!!!!!!!!!!!");
        Long id = -1L;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            log.info(verifier.verify(token).getClaim("id").asLong()+"xxxxxxxxxxxxxxxxxxxxxxxxx");
            id = verifier.verify(token).getClaim("id").asLong();
            System.out.println(id+"getcalim=====");
        } catch (Exception e) {
            return id;
        }
        return id;
    }


}
