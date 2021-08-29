package ccs.education.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ccs.education.login.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer>{

    // bcrypt形式のハッシュをcrypt関数のsaltに渡すと、引数で渡したパスワードと比較できる機能を利用して認証を行っている
    // LoginAuthenticationProvider内で認証結果を取得する際に使用する
    @Query(value = "select (password = crypt(?2 , password)) as result from account where id = ?1", nativeQuery = true)
    boolean checkIdAndPassword(String username, String password);

    // LoginService内でデータを取得する際に使用する
    Account findById(String id);
}