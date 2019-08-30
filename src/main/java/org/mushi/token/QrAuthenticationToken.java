package org.mushi.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class QrAuthenticationToken extends BaseAuthenticationToken{

    public QrAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public QrAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities, principal, credentials);
    }
}
