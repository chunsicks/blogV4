package org.example.springv3.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.springv3.user.User;

import java.sql.Date;

public class JwtUtil {
    public static String create(User user){
        String accessToken = JWT.create()
                .withSubject("바보")//별로 안중요함 보라색의 첫번째 부분
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24*7))//시간 중요함!!   토큰을 탈취했는데 상태 저장을 안해둠(얼굴이라도 저장) 할 수 있는 거는 싸인 맞네 (유효기간이 없으면 영원히) 그래서 짧게 잡음 10분 20분
                .withClaim("id", user.getId())  //payload자리
                .withClaim("username", user.getUsername())  //이거 없었는데 이거 넣으면 또 달라질거임
                .sign(Algorithm.HMAC512("metacoding"));//지금은 RSA필요 없음 내가 암호하고 내가 확인하니까
        //시크릿이 중요하다 무조건!!
        return accessToken;
    }

    //이게 뭐하는 거라고? 검증코드
    public static User verify(String jwt){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("metacoding")).build().verify(jwt);
        int id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();

        return User.builder()
                .id(id)
                .username(username)
                .build();
    }
}
