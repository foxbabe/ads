package com.sztouyun.advertisingsystem.api.account;


import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.viewmodel.account.LoginRequest;
import com.sztouyun.advertisingsystem.web.security.JwtTokenUtil;
import com.sztouyun.advertisingsystem.web.security.MyUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Api(value = "授权接口")
@RestController
@RequestMapping("/")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @ApiOperation(value = "用户登录", notes = "创建者：李川")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public InvokeResult<String> createAuthenticationToken(@RequestBody LoginRequest request){
        authenticate(request.getUsername(),request.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return InvokeResult.SuccessResult(token);
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            String message = e.getMessage();
            if(message.equals("Bad credentials")){
                message ="用户名或密码错误！";
            }
            throw new BusinessException(message);
        }
    }
}
