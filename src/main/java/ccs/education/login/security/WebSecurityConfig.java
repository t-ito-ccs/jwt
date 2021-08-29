package ccs.education.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ccs.education.login.service.LoginService;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.accesstoken.secretkey}")
    private String accessTokenSecret;

    private LoginService loginService;

    public WebSecurityConfig(LoginService loginService) {
        this.loginService = loginService;
    }

    //(1) Spring Securityを使うための設定
    @Override
    public void configure(WebSecurity web) throws Exception {
        //  (2) 全体に対するセキュリティ設定を行う
        //  web.ignoring().antMatchers("/css/**","/js/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // (3) URL毎へのセキュリティ設定を行う
        http
        .headers().frameOptions().sameOrigin()
        .and()
            .csrf().disable() // csrfは使用しない
            .authorizeRequests() // refreshTokenの処理とtopのみ認証が必要ない、それ以外は認証が必要
                .antMatchers("/api/refreshToken","/")
                    .permitAll()
                .anyRequest()
                    .authenticated()
        .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), loginService)) // JWT用の認可処理のフィルター
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), accessTokenSecret)) // JWT用の認証処理のフィルター
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // REST APIのみなので、statelessの設定
        ;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // (4) 主に認証方法の実装の設定を行う
        // AuthenticationManager
        // (4).デフォルトで呼ばれるjdbcAuthenticationから呼ばれるUsserDetailsServiceから認証を行う場合
        // パスワードの検証時に使用するEncoderも設定する
        auth.userDetailsService(getUserDetailService()).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    UserDetailsService getUserDetailService() {
        // 独自のUserDetailService
        // jdbcAuthenticationのユーザ情報取得を行うサービス
        return new LoginService(getPasswordEncoder());
    }

    @Bean
    BCryptPasswordEncoder getPasswordEncoder() {
        // パスワードの検証時に使用するハッシュ関数
        return new BCryptPasswordEncoder();
    }
}
