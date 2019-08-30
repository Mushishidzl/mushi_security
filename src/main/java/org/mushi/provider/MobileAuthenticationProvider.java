package org.mushi.provider;

import org.mushi.token.MobileAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MobileAuthenticationProvider extends BaseAbstractUserDetailsAuthenticationProvider{


    private UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {
        if(authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("MobileAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            String mobile = authentication.getPrincipal().toString();

            if(!checkSmsCode(mobile,presentedPassword)){
                this.logger.debug("Authentication failed: verifyCode does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("MobileAuthenticationProvider.badCredentials", "Bad verifyCode"));
            }
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(user.getAuthorities(),principal, authentication.getCredentials());
        mobileAuthenticationToken.setDetails(authentication.getDetails());
        return mobileAuthenticationToken;
    }

    @Override
    protected UserDetails retrieveUser(String mobile, Authentication var2) throws AuthenticationException {
        UserDetails userDetails;
        try {
             userDetails = this.userDetailsService.loadUserByUsername(mobile);
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
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }


    /**
     *
     * @param mobile 手机号
     * @param presentedPassword   验证码
     * @return
     */
    private boolean checkSmsCode(String mobile,String presentedPassword){
        // 验证码校验，调用远程服务
        String smsCode ="10000";
        return smsCode.equals(presentedPassword);
    }


}
