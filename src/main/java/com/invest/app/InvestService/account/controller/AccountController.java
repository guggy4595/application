package com.invest.app.InvestService.account.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.invest.app.domain.account.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import com.invest.app.InvestService.account.service.AccountService;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;
    @GetMapping(value= "/login")
    public String goLoing(@RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "exception", required = false) String exception,
                          Model model){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "account/login.html";
    }

    @GetMapping(value = "/signup")
    public String goSignup() {
        return "account/signup.html";
    }

    @PostMapping(value = "/goSignup")
    @ResponseBody
    public int signup(AccountDto account) {
        return accountService.saveAccount(account);
    }

    @PostMapping(value = "/searchId" )
    @ResponseBody
    public boolean searchId(@RequestParam("accountId")String acid) {
        return accountService.searchId(acid);
    }

    @PostMapping(value = "/idUserYn" )
    @ResponseBody
    public int idUserYn(@RequestParam("accountId")String acid) {
        return accountService.idUserYn(acid);
    }
}
