package com.sztouyun.advertisingsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfig {
    @Value("${spring.profiles.active}")
    private String env;

    private final String LOCAL_ENV ="local";
    private final String DEV_ENV ="dev";
    private final String TEST_ENV ="test";
    private final String STAGE_ENV ="stage";
    private final String ONLINE_ENV ="online";

    public boolean isDebug(){
        return env.equals(LOCAL_ENV) || env.equals(DEV_ENV) ||env.equals(TEST_ENV);
    }

    public boolean isLocal(){
        return env.equals(LOCAL_ENV);
    }

    public boolean isOnline(){
        return env.equals(ONLINE_ENV);
    }

    public boolean isDev(){
        return env.equals(DEV_ENV);
    }

    public boolean isTest(){
        return env.equals(TEST_ENV);
    }

    public boolean isStage(){
        return env.equals(STAGE_ENV);
    }

    public String getEnvironment(){
        return env;
    }
}
