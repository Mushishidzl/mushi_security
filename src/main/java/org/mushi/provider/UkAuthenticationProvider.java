package org.mushi.provider;

import org.mushi.token.QrAuthenticationToken;
import org.mushi.token.UKAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UkAuthenticationProvider extends BaseAbstractUserDetailsAuthenticationProvider{


    private UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {
        // Uk的验签
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UKAuthenticationToken ukAuthenticationToken = new UKAuthenticationToken(user.getAuthorities(),principal, authentication.getCredentials());
        ukAuthenticationToken.setDetails(authentication.getDetails());
        return ukAuthenticationToken;
    }

    @Override
    protected UserDetails retrieveUser(String ukcode, Authentication var2) throws AuthenticationException {
        UserDetails userDetails;
        try {
            userDetails = this.userDetailsService.loadUserByUsername(ukcode);
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
        return UKAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
