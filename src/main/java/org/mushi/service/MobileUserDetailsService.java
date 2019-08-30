package org.mushi.service;

import org.mushi.model.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MobileUserDetailsService extends BaseUserDetailsService{


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected BaseUser getUser(String str) {
        // 调用远程接口
        BaseUser baseUser = new BaseUser();
        baseUser.setId("24235346");
        baseUser.setUserName("1777777777");
        baseUser.setPassword(this.passwordEncoder.encode("123456"));
        baseUser.setActive(1);
        return baseUser;
    }
}
