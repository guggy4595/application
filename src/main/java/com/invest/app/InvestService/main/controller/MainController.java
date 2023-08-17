package com.invest.app.InvestService.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /*
    * 홈화면 호출 컨트롤러
    * */
    @GetMapping(value= {"/" , "/home"})
    public String goMain(){
        return "main.html";
    }
}
