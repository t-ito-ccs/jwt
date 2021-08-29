package ccs.education.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginToken {
    private String accessToken; // アクセストークン
    private String refreshToken; // リフレッシュトークン
}