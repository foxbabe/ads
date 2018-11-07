package com.sztouyun.advertisingsystem.controller;

import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected UserViewModel getUser() {
        return AuthenticationService.getUser();
    }
}
