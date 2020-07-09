package de.webprojekt.conf.auth.jwt;

import de.webprojekt.repository.UserRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;

public class JWTWT2Realm extends AuthorizingRealm implements Realm {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTShiroToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        final JWTShiroToken jwToken = (JWTShiroToken) token;

        if (JWTUtil.validateToken(jwToken.getToken())) {
            return new SimpleAccount(jwToken.getPrincipal(), jwToken.getCredentials(), getName());
        }

        throw new AuthenticationException();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String user=(String) principals.getPrimaryPrincipal();
        String role=userRepository.getUserById(user).getRole();
        return new AuthorizationInfo() {
            @Override
            public Collection<String> getRoles() {
                if ("admin".equals(role)) {
                    return Collections.singleton("admin");
                }
                return Collections.emptyList();
            }

            @Override
            public Collection<String> getStringPermissions() {
                return Collections.emptyList();
            }

            @Override
            public Collection<Permission> getObjectPermissions() {
                return Collections.emptyList();
            }
        };

    }

}