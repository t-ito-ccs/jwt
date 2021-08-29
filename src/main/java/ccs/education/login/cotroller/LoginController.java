package ccs.education.login.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ccs.education.login.service.LoginService;

@RestController
class LoginController {

    @Value("${jwt.accesstoken.secretkey}")
    private String accessTokenSecret;

    @Autowired
    LoginService loginService;

    @GetMapping(path = "/", produces = "text/html")
    public String top() {
        // トップ画面
        return "Top";
    }
}