package com.sztouyun.advertisingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends BaseController{
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/fillMobile")
    public String fillMobile() {
        return "other/fillMobile";
    }
}
