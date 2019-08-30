package org.mushi.service;

import org.mushi.model.BaseUser;
import org.mushi.model.BaseUserDetail;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;


@Configuration
public abstract class BaseUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser baseUser = getUser(username);

        // 用户权限
        List<GrantedAuthority> authorities = new ArrayList();

        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User user =  new org.springframework.security.core.userdetails.User(baseUser.getUserName(),
                baseUser.getPassword(), isActive(baseUser.getActive()), true, true, true, authorities);
        return new BaseUserDetail(baseUser, user);
    }


    /**
     *
     * @param str 用户名称、手机号、二维码信息、UK信息
     * @return
     */
    protected abstract BaseUser getUser(String str) ;

    /**
     * 是否可用
     * @param active
     * @return
     */
    private boolean isActive(int active){
        return active == 1 ? true : false;
    }

}
