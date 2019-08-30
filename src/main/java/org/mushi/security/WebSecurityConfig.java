package org.mushi.security;



import org.mushi.filter.BaseLoginAuthenticationFilter;
import org.mushi.filter.MobileLoginAuthenticationFilter;
import org.mushi.filter.QrLoginAuthenticationFilter;
import org.mushi.filter.UKLoginAuthenticationFilter;
import org.mushi.handler.BaseAuthenticationFailureHandler;
import org.mushi.handler.BaseAuthenticationSucessHandler;
import org.mushi.provider.MobileAuthenticationProvider;
import org.mushi.provider.QrAuthenticationProvider;
import org.mushi.provider.UkAuthenticationProvider;
import org.mushi.service.MobileUserDetailsService;
import org.mushi.service.QrUserDetailsService;
import org.mushi.service.UKUserDetailsService;
import org.mushi.service.UserNameUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserNameUserDetailsService usernameUserDetailService;

    @Autowired
    private MobileUserDetailsService mobileUserDetailsService;

    @Autowired
    private QrUserDetailsService qrUserDetailService;

    @Autowired
    private UKUserDetailsService ukUserDetailsService;



    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(getBaseLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(getMobileLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(getQrLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(getUKLoginAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage("/login").permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(mobileAuthenticationProvider());
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(qrAuthenticationProvider());
        auth.authenticationProvider(ukAuthenticationProvider());
    }

    @Bean
    public BCryptPasswordEncoder myEncoder(){
        return new BCryptPasswordEncoder(6);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider1 = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider1.setUserDetailsService(usernameUserDetailService);
        // 禁止隐藏用户未找到异常
        provider1.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider1.setPasswordEncoder(myEncoder());
        return provider1;
    }


    @Bean
    public MobileAuthenticationProvider mobileAuthenticationProvider(){
        MobileAuthenticationProvider provider = new MobileAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(mobileUserDetailsService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public QrAuthenticationProvider qrAuthenticationProvider(){
        QrAuthenticationProvider provider = new QrAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(qrUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public UkAuthenticationProvider ukAuthenticationProvider(){
        UkAuthenticationProvider provider = new UkAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(ukUserDetailsService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }



    @Bean
    public MobileLoginAuthenticationFilter getMobileLoginAuthenticationFilter() {
        MobileLoginAuthenticationFilter filter = new MobileLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(new BaseAuthenticationSucessHandler());
        filter.setAuthenticationFailureHandler(new BaseAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public QrLoginAuthenticationFilter getQrLoginAuthenticationFilter() {
        QrLoginAuthenticationFilter filter = new QrLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(new BaseAuthenticationSucessHandler());
        filter.setAuthenticationFailureHandler(new BaseAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public UKLoginAuthenticationFilter getUKLoginAuthenticationFilter() {
        UKLoginAuthenticationFilter filter = new UKLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(new BaseAuthenticationSucessHandler());
        filter.setAuthenticationFailureHandler(new BaseAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public BaseLoginAuthenticationFilter getBaseLoginAuthenticationFilter() {
        BaseLoginAuthenticationFilter filter = new BaseLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(new BaseAuthenticationSucessHandler());
        filter.setAuthenticationFailureHandler(new BaseAuthenticationFailureHandler());
        return filter;
    }
}
