package com.SzTouYun.advertisingsystem;

import com.sztouyun.advertisingsystem.AdvertisingSystemApplication;
import com.sztouyun.advertisingsystem.api.account.UserApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertisingSystemApplication.class)
public class UserApiControllerTest {

    @Autowired
    private UserApiController userApiController;

    @Test
    public void sendMessageWithForgetPasswordTest(){

    }
}
