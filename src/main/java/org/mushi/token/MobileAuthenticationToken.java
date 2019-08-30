package org.mushi.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MobileAuthenticationToken extends BaseAuthenticationToken{


    public MobileAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MobileAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities, principal, credentials);
    }
}
