package ccs.education.login.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import ccs.education.login.dto.LoginToken;
import ccs.education.login.service.LoginService;

@RestController
@RequestMapping("/api")
class LoginApiController {

    @Value("${jwt.accesstoken.secretkey}")
    private String accessTokenSecret;

    @Autowired
    LoginService loginService;

    @GetMapping(path = "/hello")
    public String hello() {
        // Helloを表示する画面(ヘッダーに有効なaccessTokenがないとエラーとなる)

        return "Hello";
    }

    @PostMapping("/refreshToken")
    public LoginToken refreshToken(@RequestBody LoginToken token) {
        // accessTokenの再作成を行う、すでに登録済みのrefreshTokenが無いと再作成は行わない

        final DecodedJWT jwt = JWT.decode(token.getAccessToken());
        try {
            JWT.require(Algorithm.HMAC512(accessTokenSecret.getBytes())).build().verify(jwt);
        } catch (TokenExpiredException e) {
            // allow expired access token for user authentication
        } catch (Exception e) {
            throw e;
        }

        if (loginService.verifyRefreshToken(jwt.getSubject(), token.getRefreshToken())) {
            return loginService.issueToken(jwt.getSubject());
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

}