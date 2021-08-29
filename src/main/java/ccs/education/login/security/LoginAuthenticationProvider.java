package ccs.education.login.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ccs.education.login.repository.AccountRepository;

public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    // UserDetailに設定するダミーのパスワード
    private static final String DUMMY_PASSWORD = "DUMMY_PASSWORD";
    // 今回のデモではすべてユーザ権限とする
    private static final List<GrantedAuthority> AUTH_USER = AuthorityUtils.createAuthorityList("USER");

    @Autowired
    AccountRepository repository;

    //追加の認証処理、今回は使わないので空とする
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    //認証処理
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //SQLを独自設定し、比較する場合の設定
        //PostgreSQLのハッシュ関数で受け取ったパスワードと一致するか確認を行っている
        boolean isValid = false;
        isValid = repository.checkIdAndPassword(username, authentication.getCredentials().toString());
        if (!isValid) {
            throw new UsernameNotFoundException(username);
        }
        return new User(username, DUMMY_PASSWORD, AUTH_USER);
    }
}
