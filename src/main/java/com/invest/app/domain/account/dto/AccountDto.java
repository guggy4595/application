package com.invest.app.domain.account.dto;

import lombok.*;
import com.invest.app.utilty.constant.AccountRole;
import com.invest.app.domain.account.entity.AccountEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String accountId;
    private String accountName;
    private String accountEmail;
    private String accountPassword;
    private String accountRole;
    public AccountEntity toEntity(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return AccountEntity.builder()
                .accountId(this.accountId)
                .accountName(this.accountName)
                .accountEmail(this.accountEmail)
                .accountPassword(passwordEncoder.encode(  this.accountPassword ))
                .accountRole(AccountRole.USER)
                .build();
    }
}
