package ccs.education.login.dto;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import ccs.education.login.entity.Account;
import lombok.Getter;

@Getter
public class LoginUser extends User {

    private Account account; // アカウント情報

    // Account情報をDTOクラスに設定する、今回のデモではすべてユーザ権限とする
    public LoginUser(Account account) {
        super(account.getId(), account.getPassword(), AuthorityUtils.createAuthorityList("USER"));
        this.account = account;
    }
}