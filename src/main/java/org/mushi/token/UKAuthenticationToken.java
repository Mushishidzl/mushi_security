package org.mushi.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UKAuthenticationToken extends BaseAuthenticationToken{

    public UKAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UKAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities, principal, credentials);
    }
}
