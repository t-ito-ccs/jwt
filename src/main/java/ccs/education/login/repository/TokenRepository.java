package ccs.education.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ccs.education.login.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer>{

    // TokenRepository内でデータを取得する際に使用する
    Token findById(String id);
}