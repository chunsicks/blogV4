package org.example.springv3.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.springv3.core.util.JwtUtil;
import org.example.springv3.core.util.Resp;
import org.example.springv3.user.User;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("JwtAuthorizationFilter 필터가 동작했습니다");
        //다운케스팅
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        //이게 널이면? 인생끝난거임
        String accessToken = req.getHeader("Authorization");
        //euals""하면 되기는 하는데 위험함 스페이스바, 두칸띄우기 못함 그래서 length() <1
        //이래쓰면 힘듬  isEmpty()가면 길이가 0이면 되어있다 하지만 null 검증 못함
        //is blank()는 white spase검증함! 둘 다 검증함 이거 사용해야함
        if(accessToken == null || accessToken.isBlank()){
            //resp 해줘야 함
            //System.out.println("토근이 없어요"); 1초짜리 토큰 만들어서 연습한다
            //return; 해야함 else가 없으니까
            resp.setContentType("application/json; charset=utf-8");
            PrintWriter out = resp.getWriter();
            //인가 안된것은 맞으나 실질적으로는 인증 안됐으니 401 쓴다
            Resp fail = Resp.fail(401, "토큰이 없어요");
            //json으로 바꿔줘야하니까
            String responseBody = new ObjectMapper().writeValueAsString(fail);
            out.println(responseBody);
            out.flush();
            return;
            //여기 왜 체인.doFilter안하냐 ? 끝내고 바로 실행해야 하니까 else있으면 return 안써도 됨
        }

        try{
            //검증
            //언제 오류나나?  1. 서명 위조 혹 만료됐을 때  트라이 케치로 묶어야 한다! 안하면 500 터짐 제어해야함!
            System.out.println("0");
            User sessionUser = JwtUtil.verify(accessToken);
            System.out.println("1");
            HttpSession session = req.getSession();
            System.out.println("2");
            session.setAttribute("sessionUser", sessionUser);
            System.out.println("3");
            filterChain.doFilter(req,resp);
        } catch (Exception e) {
            //json으로 바꿀 꺼임 이거 setHeader과 같다!!  set해더가 공부가 더 됨
            //setHeader로 바꾸면 귀찮아짐
            //resp.setHeader("Content_Type","application/json; charset=utf-8");
            resp.setContentType("application/json; charset=utf-8");
            PrintWriter out = resp.getWriter();
            //인가 안된것은 맞으나 실질적으로는 인증 안됐으니 401 쓴다
            Resp fail = Resp.fail(401, e.getMessage());
            //json으로 바꿔줘야하니까
            String responseBody = new ObjectMapper().writeValueAsString(fail);
            out.println(responseBody);
            out.flush();
            //이러면 될까? 안됨  통신의 기본은 변환해서 보내야 함  fail은 자바 객체니까 안됨!!
        }


//        //ds가 이미 해서 전달해준다
//
//        resp.setHeader("Content-Type", "text/plain");
//        //헬스장 가면 request, 집에갈 때 response
//        //이게 쓰기 버퍼다   jsp에서는 out이라한다!   응답의 쓰기 버퍼
//        PrintWriter out = resp.getWriter();
//        out.print("good");
//        out.flush();
    }
    //filterChain  다음 체인으로 간다 만약 없으면 DS로 간다!
}
