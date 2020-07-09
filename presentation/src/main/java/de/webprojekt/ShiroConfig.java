package de.webprojekt;

import de.webprojekt.conf.auth.BasicAuthenticationFilterWithoutRedirect;
import de.webprojekt.conf.auth.FormAuthenticationFilterWithoutRedirect;
import de.webprojekt.conf.auth.LogoutFilterWithoutRedirect;
import de.webprojekt.conf.auth.jwt.JWTAuthenticationFilter;
import de.webprojekt.conf.auth.jwt.JWTWT2Realm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public Realm jwtRealm() {
        return new JWTWT2Realm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm, Realm jwtRealm) {
        return new DefaultWebSecurityManager(Arrays.asList(jwtRealm, realm));
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();

        filters.put("restAuthenticator", new BasicAuthenticationFilterWithoutRedirect());
        filters.put("loginFilter", new FormAuthenticationFilterWithoutRedirect());
        filters.put("logoutFilter", new LogoutFilterWithoutRedirect());
        filters.put("jwtFilter", new JWTAuthenticationFilter());


        final Map<String, String> chainDefinition = new LinkedHashMap<>();

        // configuration for stateless authentication on each request
        //chainDefinition.put("/rest/auth/basic/**", "noSessionCreation, restAuthenticator");

        // configuration for JWT based authentication
        chainDefinition.put("/rest/auth/authenticate", "anon");
        chainDefinition.put("/rest/auth/**", "noSessionCreation, jwtFilter");

        // configuration for using session based authentication
       // chainDefinition.put("/login.jsp", "loginFilter");
       // chainDefinition.put("/logout", "logoutFilter");

        // configuration for stateless authentication on each request
       // chainDefinition.put("/rest/auth/**", "restAuthenticator");

        // make other examples not require authentication
        chainDefinition.put("/rest/**", "anon");

        // make static Angular resources globally available
        chainDefinition.put("/**", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition);

        return shiroFilterFactoryBean;
    }

}
