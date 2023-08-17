package com.invest.app.InvestService.account.service;

import com.invest.app.domain.account.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.util.StringUtils;
import org.springframework.stereotype.Service;
import com.invest.app.domain.account.dto.AccountDto;
import com.invest.app.domain.account.entity.AccountEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.invest.app.InvestService.account.repository.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String acid ) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findByaccountId(acid);

        if(accountEntity == null) {
            log.error("존재하지 않는 회원");
            throw new UsernameNotFoundException(acid);
        }

        return User.builder()
                .username(accountEntity.getAccountId())
                .password(accountEntity.getAccountPassword())
                .roles(String.valueOf(accountEntity.getAccountRole()))
                .build();
    }

    public boolean isLogin(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if( principal.equals("anonymousUser") ){
            return false;
        }else{
            return true;
        }
    }

    public String getUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        if( principal.equals("anonymousUser") ){
            return "";
        }else{
            return userDetails.getUsername();
        }
    }

    public boolean searchId (String acid) {
        AccountEntity accountEntity = accountRepository.findByaccountId(acid);
        boolean result = true;
        try {if(accountEntity.getAccountId() != null) {result = false;}}
        catch (Exception e) {result = true;}
        return result;
    }

    public int idUserYn (String acid) {
        AccountEntity accountEntity = accountRepository.findByaccountId(acid);
        int result = 0;

        if(accountEntity != null) {
            if(StringUtils.equals("N",accountEntity.getAccountUseYn())) {
                result = 2; // 휴면 계정
            } else {
                result = 0000; // 정상
            }
        } else {
            result = 4; // 미존재계정
        }
        return result;
    }

    public int saveAccount (AccountDto dto) {
        int result = 0000;

        if(StringUtils.equals("",dto.getAccountId()) || StringUtils.equals("",dto.getAccountPassword()) || StringUtils.equals("",dto.getAccountName()) || StringUtils.equals("",dto.getAccountEmail())) {
            return 1;
        }
        if(!dto.getAccountId().matches("^[a-z]+[a-z0-9]{5,19}$")) {
            return 2;
        }
        if(!dto.getAccountPassword().matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$")) {
            return 3;
        }
        if(!dto.getAccountName().matches("^[ㄱ-ㅎ|가-힣|a-z|A-Z|]+$")) {
            return 4;
        }
        if(!dto.getAccountEmail().matches("^[a-z0-9]+@[a-z]+\\.[a-z]{2,3}$")) {
            return 5;
        }
        try {
            accountRepository.save(dto.toEntity());
        }
        catch (Exception e) {result = 9999;}
        return result;
    }
}
