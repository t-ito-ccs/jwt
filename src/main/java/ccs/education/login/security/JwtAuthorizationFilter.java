package ccs.education.login.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String TOKEN_PREFIX = "Bearer ";

    private String accessTokenSecretKey;

    public JwtAuthorizationFilter(AuthenticationManager authManager, String accessTokenSecretKey) {
        super(authManager);
        this.accessTokenSecretKey = accessTokenSecretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // ヘッダーの確認を行う、決まったHeaderの記載がなければ次のFilterの呼び出しを行う

        final String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            // ヘッダーがnullもしくは、Bearerの記載がない場合は次のフィルターへ
            chain.doFilter(req, res);
            return;
        }

        // JWTによる認証処理
        final UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // ヘッダーからaccessTokenを取得し有効か確認する

        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            final String user = JWT.require(Algorithm.HMAC512(accessTokenSecretKey.getBytes()))
                    .build()
                    .verify(StringUtils.substringAfter(token, TOKEN_PREFIX))
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
        }
        return null;
    }
}