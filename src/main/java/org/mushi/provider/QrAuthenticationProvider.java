package org.mushi.provider;

import org.mushi.token.QrAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class QrAuthenticationProvider extends BaseAbstractUserDetailsAuthenticationProvider{


    private UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {
        // 二维码校验只需要验证用户就可以
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        QrAuthenticationToken qrAuthenticationToken = new QrAuthenticationToken(user.getAuthorities(),principal, authentication.getCredentials());
        qrAuthenticationToken.setDetails(authentication.getDetails());
        return qrAuthenticationToken;
    }

    @Override
    protected UserDetails retrieveUser(String qrcode, Authentication var2) throws AuthenticationException {
        UserDetails userDetails;
        try {
            userDetails = this.userDetailsService.loadUserByUsername(qrcode);
        }catch (UsernameNotFoundException ue){
            throw ue;
        }catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
        if(userDetails == null){
            throw new InternalAuthenticationServiceException("");
        }else{
            return userDetails;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return QrAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
