package io.github.rimonmostafiz.service;

import io.github.rimonmostafiz.config.properties.JwtConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisTokenService {

    private final JwtConfigProperties jwtConfigProperties;

    public void addTokenToWhiteList(String accessToken, String refreshToken) {
//        String wlAccessTokenKey = WHITE_LIST_ACCESS_TOKEN_PREFIX + accessToken;
//        String wlRefreshTokenKey = WHITE_LIST_REFRESH_TOKEN_PREFIX + refreshToken;

//        log.debug("white list -> access_token Key : {}", wlAccessTokenKey);
//        log.debug("white list -> refresh_token Key : {}", wlRefreshTokenKey);

//        long accTokValInMilli = TimeUnit.MINUTES.toMillis(accessTokenValidity);
//        long refTokValInMilli = TimeUnit.MINUTES.toMillis(refreshTokenValidity);

//       redisTemplate.opsForValue().set(wlAccessTokenKey, accessToken, accTokValInMilli, TimeUnit.MILLISECONDS);
//       redisTemplate.opsForValue().set(wlRefreshTokenKey, refreshToken, refTokValInMilli, TimeUnit.MILLISECONDS);
    }

    public void addTokenToWhiteList(String accessToken) {
//        String wlAccessTokenKey = WHITE_LIST_ACCESS_TOKEN_PREFIX + accessToken;
//        long accTokValInMilli = TimeUnit.MILLISECONDS.toMillis(accessTokenValidity);
//        redisTemplate.opsForValue().set(wlAccessTokenKey, accessToken, accTokValInMilli, TimeUnit.MILLISECONDS);
    }
}
