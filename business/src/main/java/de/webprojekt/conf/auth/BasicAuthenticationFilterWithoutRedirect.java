package de.webprojekt.conf.auth;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilterWithoutRedirect extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        /** set no {@link AUTHENTICATE_HEADER}-header, so the browser does nothing **/
        WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
