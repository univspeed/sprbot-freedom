package com.cybercloud.sprbotfreedom.platform.util;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * @author: liuyutang
 */
@Slf4j
@Component
public class JwtUtil {


    private static boolean isCheckLogin;

    @Value("${system.token.header:authorization}")
    private String headerKey;
    @Value("${system.token.expire-time:3600}")
    private int expireTime;
    @Value("${system.token.secret:cybercloud_token}")
    private String secrect;
    @Value("${system.token.open-check:true}")
    public void setIsCheckLogin(boolean isCheckLogin) {
        JwtUtil.isCheckLogin = isCheckLogin;
    }
    private static final String ISS = "cybercloud";
    /**
     * 记住我后的过期时间 默认7天
     */
    private static final long EXPIRATION_REMEMBER = 60*60*24*7;


    public static JwtUtil jwtUtil;

    @PostConstruct
    public void init(){
        jwtUtil = this;
        jwtUtil.headerKey = this.headerKey;
        jwtUtil.expireTime = this.expireTime;
        jwtUtil.secrect = this.secrect;
    }

    /**
     * 创建token
     *
     * @param claims     用户信息
     * @param attr       额外信息
     * @param rememberMe 是否记住我，true 过期时间延期到7天
     * @return
     */
    public static String createToken(Map<String, Object> claims, String attr, boolean rememberMe) {
        long expiration = rememberMe ? EXPIRATION_REMEMBER : jwtUtil.expireTime;
        Date time = new Date();
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtUtil.secrect)
                .setIssuer(ISS)
                .setClaims(claims)
                .setId(attr)
                .setIssuedAt(time)
                .setExpiration(new Date(time.getTime() + expiration * 1000))
                .compact();
        return token;
    }
    public static String createToken(Map<String, Object> claims, String attr) {
        Date time = new Date();
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtUtil.secrect)
                .setIssuer(ISS)
                .setClaims(claims)
                .setId(attr)
                .setIssuedAt(time)
                .setExpiration(new Date(time.getTime() + jwtUtil.expireTime * 1000))
                .compact();
        return token;
    }
    public static String refreshToken(String attr, String token, boolean rememberMe) {
        try {
            return createToken(getTokenBody(token), attr, rememberMe);
        } catch (ExpiredJwtException expiredJwtException) {
            return createToken(getTokenBody(token), attr, rememberMe);
        }
    }

    public static Jws<Claims> getJwt(String jwtToken) {

        return Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(jwtUtil.secrect.getBytes()) //设置签名的秘钥
                .parseClaimsJws(jwtToken);
    }


    public static String get(String token, String key, boolean ignoreExpired) {
        try {
            return String.valueOf(getTokenBody(token).get(key));
        } catch (ExpiredJwtException expiredJwtException) {
            if (ignoreExpired) {
                return expiredJwtException.getClaims().get(key, String.class);
            } else {
                throw new ExpiredJwtException(expiredJwtException.getHeader(), expiredJwtException.getClaims(), "令牌已过期");
            }
        }
    }

    /**
     * 从token中获取其它信息
     *
     * @param token
     * @return
     */
    public static String getAttr(String token) {
        return getTokenBody(token).getId();
    }

    /**
     * 判断是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        try {
            return getTokenBody(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }

    }

    /**
     * 判断是否已过期,同时判断其它附加信息是否一致
     *
     * @param token
     * @param attr  附加信息
     * @return
     */
    public static boolean validToken(String token, String attr) {
        boolean bool = false;
        try {
            Claims body = getTokenBody(token);
            //是否过期
            boolean before = body.getExpiration().before(new Date());
            if (!before) {
                //附加信息是否相同
                if ((attr == null && body.getId() == null) || (attr != null && attr.equals(body.get("jti")))) {
                    bool = true;
                }
            }
        } catch (Exception e) {

        }
        return bool;

    }

    /**
     * 获取jwt中负载信息
     *
     * @param token
     * @return
     */
    public static Claims getTokenBody(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(jwtUtil.secrect)
                    .parseClaimsJws(token)
                    .getBody();
            return body;
        }catch (Exception e){
            log.error("{}",e);
            ServiceException.throwError(SystemErrorCode.ERROR_410);
        }
        return null;
    }

    public static UserInfo getUserInfo(String token){
        Claims body = getTokenBody(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(body.get("id", Long.class));
        userInfo.setUsername(body.get("username", String.class));
        userInfo.setRoleId(body.get("roleId", Long.class));
        userInfo.setRoleName(body.get("roleName", String.class));
        return userInfo;
    }

    public static void main(String[] args) {
        String token =  "eyJhbGciOiJIUzUxMiJ9.eyJvcmdOYW1lIjoi5bu66K6-5Y2V5L2NMDAxIiwibW9iaWxlIjoiIiwiaXNUb3BMZXZlbCI6IjAiLCJ1c2VyTmFtZSI6ImNmX2pzcXkiLCJvcmdJZCI6IjkxZDQ1NzYyLTkzMWYtNDE4My1hMGU2LTc4NTllNTk1NDFjNSIsImxhc3RMb2dpbklwIjpudWxsLCJyZWFsTmFtZSI6ImNmX2pzcXkiLCJyZWdpb25Db2RlIjoiNjIwMDAwIiwic3lzdGVtIjoicGMiLCJwYXJlbnRPcmdJZCI6bnVsbCwiaWQiOiIyNzBhYWE0YS0xOGYzLTQ3YWMtODk2OS0xYTk0YzkwMDZiMmIiLCJleHAiOjE2MTc4MzAwNDQsImlzVG9NaW5pc3RyeSI6IjAiLCJpYXQiOjE2MTc3ODY4NDQsImp0aSI6IjU5LjE3NS43Mi42NCJ9.VWSggA1HI0Rb9dsi8C7QyC2CMA6d-J0gbGqtdNLlyhEXahnos1E7U7K3hb9pCSb1T5JFP1StMbqfAQBy67u1sw";
        System.out.println(JwtUtil.get(token,"orgId",true));
        System.out.println(JwtUtil.get(token,"username",true));

    }
}
