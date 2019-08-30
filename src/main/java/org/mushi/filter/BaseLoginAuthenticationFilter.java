package org.mushi.filter;

import org.mushi.token.MobileAuthenticationToken;
import org.mushi.token.QrAuthenticationToken;
import org.mushi.token.UKAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public static final String SPRING_SECURITY_RESTFUL_TYPE_PHONE = "phone";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_QR = "qr";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_UK = "uk";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_DEFAULT = "user";

    // 登陆类型：user:用户密码登陆；phone:手机验证码登陆；qr:二维码扫码登陆; uk:UK登录
    private static final String SPRING_SECURITY_RESTFUL_TYPE_KEY = "type";

    // 登陆终端：1：移动端登陆，包括微信公众号、小程序、APP等；0：PC后台登陆
    private static final String SPRING_SECURITY_RESTFUL_TERMINAL_KEY = "terminal";

    private static final String SPRING_SECURITY_RESTFUL_USERNAME_KEY = "username";
    private static final String SPRING_SECURITY_RESTFUL_PASSWORD_KEY = "password";

    private static final String SPRING_SECURITY_RESTFUL_PHONE_KEY = "phone";
    private static final String SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY = "verifyCode";

    private static final String SPRING_SECURITY_RESTFUL_QR_CODE_KEY = "qrCode";

    private static final String SPRING_SECURITY_RESTFUL_UK_CODE_KEY = "ukCode";

    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/login";

    private boolean postOnly = true;

//    public BaseLoginAuthenticationFilter() {
//        super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL, "POST"));
//    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        // 登录类型
        String type = obtainParameter(request, SPRING_SECURITY_RESTFUL_TYPE_KEY);

        // 登录的终端  0：PC   1：移动端
        String terminal = obtainParameter(request, SPRING_SECURITY_RESTFUL_TERMINAL_KEY);
        AbstractAuthenticationToken authRequest;
        String principal;
        String credentials;

        // 手机验证码登陆
        if(SPRING_SECURITY_RESTFUL_TYPE_PHONE.equals(type)){
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_PHONE_KEY);
            credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY);

            principal = principal.trim();
            authRequest = new MobileAuthenticationToken(principal, credentials);
        }
        // 二维码扫码登陆
        else if(SPRING_SECURITY_RESTFUL_TYPE_QR.equals(type)){
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_QR_CODE_KEY);
            credentials = null;
            authRequest = new QrAuthenticationToken(principal, credentials);
        }
        // Uk登录
        else if (SPRING_SECURITY_RESTFUL_TYPE_UK.equals(type)){
            principal = obtainParameter(request,SPRING_SECURITY_RESTFUL_UK_CODE_KEY);
            credentials = null;
            authRequest = new UKAuthenticationToken(principal,credentials);
        }
        // 账号密码登陆
        else {
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_USERNAME_KEY);
            credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_PASSWORD_KEY);

            principal = principal.trim();
            authRequest = new UsernamePasswordAuthenticationToken(principal, credentials);

        }

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request,
                            AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        String result =  request.getParameter(parameter);
        return result == null ? "" : result;
    }
}
