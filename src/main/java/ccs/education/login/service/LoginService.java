package ccs.education.login.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import ccs.education.login.dto.LoginToken;
import ccs.education.login.dto.LoginUser;
import ccs.education.login.entity.Token;
import ccs.education.login.repository.AccountRepository;
import ccs.education.login.repository.TokenRepository;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private PasswordEncoder passwordEncoder;

    @Value("${jwt.accesstoken.expirationtime}")
    private long accessTokenExpTime;

    @Value("${jwt.refreshtoken.expirationtime}")
    private long refreshTokenExpTime;

    @Value("${jwt.accesstoken.secretkey}")
    private String accessTokenSecret;

    private static final int REFRESH_TOKEN_LENGTH = 24;


    public LoginService(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //ユーザの情報を取得し、認証処理に渡すためにUserクラスを継承したデータに入れて返却
        return  new LoginUser(accountRepository.findById(username));
    }

    @Transactional
    public LoginToken issueToken(String username) throws UsernameNotFoundException {
        // Token発行処理、refreshTokenとaccessTokenを作成し、refreshTokenを登録する

        final Instant now = Instant.now();
        final String token = JWT.create()
                .withSubject(username)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(accessTokenExpTime, ChronoUnit.SECONDS)))
                .sign(Algorithm.HMAC512(accessTokenSecret.getBytes()));

        return LoginToken.builder()
                .accessToken(token)
                .refreshToken(generateRefreshToken(username))
                .build();
    }

    @Transactional(readOnly = true)
    public boolean verifyRefreshToken(String userId, String refreshToken) throws UsernameNotFoundException {
        // refreshTokenの有効性の確認を行う

        final Token token = tokenRepository.findById(userId);

        if(token.getIssueDateTime() != null &&
            token.getIssueDateTime().plus(refreshTokenExpTime, ChronoUnit.SECONDS).isBefore(Instant.now())) {
            return false;
        }

        return StringUtils.isNotEmpty(token.getRefreshToken()) && passwordEncoder.matches(refreshToken, token.getRefreshToken());
    }

    private String generateRefreshToken(String userId) throws UsernameNotFoundException {
        // refreshTokenの生成

        final Token token = new Token();
        final String refreshToken = RandomStringUtils.randomAlphanumeric(REFRESH_TOKEN_LENGTH);
        token.setId(userId);
        token.setRefreshToken(passwordEncoder.encode(refreshToken));
        token.setIssueDateTime(Instant.now());
        tokenRepository.save(token);
        return refreshToken;
    }
}