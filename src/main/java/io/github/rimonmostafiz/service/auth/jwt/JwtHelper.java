package io.github.rimonmostafiz.service.auth.jwt;

import io.github.rimonmostafiz.component.exception.InvalidJwtAuthenticationException;
import io.github.rimonmostafiz.component.exception.UserNotFoundException;
import io.github.rimonmostafiz.config.properties.JwtConfigProperties;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.entity.db.UserRoles;
import io.github.rimonmostafiz.repository.UserRepository;
import io.github.rimonmostafiz.service.auth.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.github.rimonmostafiz.service.auth.SecurityConstants.*;
import static io.github.rimonmostafiz.utils.ResponseUtils.*;
import static io.github.rimonmostafiz.utils.SessionKey.TYPE_OF_TOKEN;
import static io.github.rimonmostafiz.utils.SessionKey.USER_DETAILS;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtConfigProperties.class)
@RequiredArgsConstructor
public class JwtHelper {

    private Key key;
    private JwtParser jwtParser;
    private final UserRepository userRepository;
    private final JwtConfigProperties jwtProperties;

//    @Qualifier("stringRedisTemplate")
//    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String createToken(String username, List<String> roles, String tokenType, Date tokenCreateTime) {
        log.debug("Token create request for username : {}, tokenType : {}, tokenCreateTime : {}",
                username, tokenType, tokenCreateTime);
        Claims claims = Jwts.claims().setSubject(username);
        Date tokenValidity;

        if (tokenType.equals(ACCESS_TOKEN)) {
            claims.put("roles", roles);
            log.debug("roles : {}", roles);
            tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(jwtProperties.getAccessExpire()));
        } else {
            log.debug("roles : {}", roles);
            tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(jwtProperties.getRefreshExpire()));
        }

        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam(TOKEN_TYPE, tokenType)
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(Claims claims, HttpServletRequest request, String token) {
        log.debug("Authentication request of token received");
        String username = getUsername(claims);
        String tokenType = getTokenType(token);
        List<String> roles;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (tokenType.equals(ACCESS_TOKEN)) {
            roles = getRoles(claims);
            log.debug("tokenType : access_token, username : {}, -> roles : {}", username, roles);
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            grantedAuthorities.addAll(simpleGrantedAuthorities);
        } else {
            List<UserRoles> userRoles = userRepository.findByUsername(username)
                    .map(User::getRoles)
                    .orElseThrow(() -> new UserNotFoundException("User Not Found"));
            roles = userRoles.stream()
                    .map(UserRoles::getRoleName)
                    .collect(Collectors.toList());
            log.debug("tokenType : refresh_token, Username : {}, -> roles : {}", username, roles);
            roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role)));
        }
        UserDetails userDetails = new UserPrincipal(username, null, roles);
        request.getSession().setAttribute(USER_DETAILS, userDetails);
        request.getSession().setAttribute(TYPE_OF_TOKEN, tokenType);
        return new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            log.debug("Trying to resolve claims token ");
            String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
            if (bearerToken != null && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
                return parseJwtClaims(bearerToken.substring(BEARER_TOKEN_PREFIX.length()));
            }
            return null;
        } catch (ExpiredJwtException ex) {
            log.debug("Could not parse jwt claims, Token Expired ", ex);
            req.setAttribute(EXPIRED, ex.getMessage());
            throw new InvalidJwtAuthenticationException(EXPIRED_TOKEN, ex);
        } catch (Exception ex) {
            log.debug("Could not parse jwt claims, Token Invalid ", ex);
            req.setAttribute(INVALID, ex.getMessage());
            throw new InvalidJwtAuthenticationException(INVALID_TOKEN, ex);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        log.debug("Resolving jwt token from request");
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
            log.debug("Bearer token found in header, returning token");
            return bearerToken.substring(BEARER_TOKEN_PREFIX.length());
        }
        log.debug("Bearer token NOT found in header, returning token : null");
        return null;
    }

    public boolean validateClaims(Claims claims) throws InvalidJwtAuthenticationException {
        try {
            log.debug("Validating jwt token Claims");
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            log.debug("Exception while parsing Claims");
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token", e);
        }
    }

//    public boolean tokenNotBlackListed(String token) {
//        log.debug("Checking if jwt token already in blacklist");
//        String key = getBlackListKeyByToken(token);
//        String value = (String) redisTemplate.opsForValue().get(key);
//        if (value == null) {
//            log.debug("Jwt token already in BLACK LIST : FALSE");
//            return true;
//        } else {
//            log.debug("Jwt token already in BLACK LIST : TRUE");
//            return false;
//        }
//    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String getTokenType(String token) {
        log.debug("Parsing token type from jwt token");
        try {
            if (token != null) {
                return (String) jwtParser.parse(token).getHeader().get(TOKEN_TYPE);
            }
            log.debug("Token is Null, returning TYPE : null");
            return null;
        } catch (ExpiredJwtException ex) {
            log.debug("Could not parse jwt claims, Token Expired ", ex);
            throw new InvalidJwtAuthenticationException(EXPIRED_TOKEN, ex);
        } catch (Exception ex) {
            log.debug("Could not parse jwt claims, Token Invalid ", ex);
            throw new InvalidJwtAuthenticationException(INVALID_TOKEN, ex);
        }
    }

    private String getBlackListKeyByToken(String token) {
        if (getTokenType(token).equals(ACCESS_TOKEN)) {
            return BLACK_LIST_ACCESS_TOKEN_PREFIX + token;
        } else {
            return BLACK_LIST_REFRESH_TOKEN_PREFIX + token;
        }
    }

//    public boolean insertTokenToBlackList(HttpServletRequest request, String tokenType) {
//        try {
//            if (tokenType.equals(ACCESS_TOKEN)) {
//                String accessToken = resolveToken(request);
//                String blAccessTokenKey = BLACK_LIST_ACCESS_TOKEN_PREFIX + accessToken;
//                redisTemplate.opsForValue().set(blAccessTokenKey, accessToken,
//                        TimeUnit.MINUTES.toMillis(jwtProperties.getAccessExpire()), TimeUnit.MILLISECONDS);
//                return true;
//            } else {
//                String refreshToken = resolveToken(request);
//                String blRefreshTokenKey = BLACK_LIST_REFRESH_TOKEN_PREFIX + refreshToken;
//                redisTemplate.opsForValue().set(blRefreshTokenKey, refreshToken,
//                        TimeUnit.MINUTES.toMillis(jwtProperties.getRefreshExpire()), TimeUnit.MILLISECONDS);
//                return true;
//            }
//        } catch (Exception ex) {
//            log.debug("Error While Insert JWT Toke To BlackList", ex);
//            return false;
//        }
//    }

    public String getPayload(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("\\."); // Splitting header, payload and signature
        return new String(decoder.decode(parts[1]));
    }
}
