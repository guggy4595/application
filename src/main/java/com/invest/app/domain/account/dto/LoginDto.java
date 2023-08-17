package com.invest.app.domain.account.dto;

import lombok.*;
import com.invest.app.domain.account.entity.AccountEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
public class LoginDto implements UserDetails {

    private long accountNumber;        // 회원번호
    private String accountId; // 회원아이디
    private String accountPassword;// 회원비밀번호
    private String accountName; // 회원 이름
    private final Set<GrantedAuthority> authorities;    // 부여된 인증들의 권한
    private Map<String, Object> attributes ; // oauth 인증된 회원의 정보

    public LoginDto(AccountEntity accountEntity , Collection< ? extends GrantedAuthority > authorityList ) {
        this.accountNumber = accountEntity.getAccountNumber();
        this.accountId = accountEntity.getAccountId();
        this.accountPassword = accountEntity.getAccountPassword();
        this.accountName = accountEntity.getAccountName();
        this.authorities = Collections.unmodifiableSet( new LinkedHashSet<>( authorityList ));
    }

    @Override public String getPassword() {return this.accountPassword;}
    @Override public String getUsername() {return this.accountId;}

    // 계정 인증 만료 여부 확인 [ true : 만료되지 않음 ]
    @Override public boolean isAccountNonExpired() {return true;}

    // 계정 잠겨 있는지 확인 [ true : 잠겨있지 않음 ]
    @Override public boolean isAccountNonLocked() {return true;}

    // 계정 패스워드가 만료 여부 확인 [ true : 만료 되지않음 ]
    @Override public boolean isCredentialsNonExpired() {return true;}

    // 계정 사용 가능한 여부 확인 [ true : 사용가능 ]
    @Override public boolean isEnabled() {return true;}

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
