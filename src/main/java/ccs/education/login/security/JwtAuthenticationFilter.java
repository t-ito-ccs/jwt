package ccs.education.login.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import ccs.education.login.dto.LoginToken;
import ccs.education.login.service.LoginService;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private LoginService loginService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, LoginService loginService) {
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        // 認証処理を行う、認証成功時はsuccessfulAuthenticationが呼ばれる
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getParameter("username"),
                        req.getParameter("password"),
                        Collections.emptyList())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
           // 認証成功時に呼ばれ、トークンを作成しresponseにjson形式で書き出して返却する

        final LoginToken issueToken = loginService.issueToken(auth.getName());
        final String json = new ObjectMapper().writeValueAsString(issueToken);

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(json);
        res.getWriter().flush();
    }
}