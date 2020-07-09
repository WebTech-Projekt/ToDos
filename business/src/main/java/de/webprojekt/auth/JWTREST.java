package de.webprojekt.auth;

import com.nimbusds.jose.JOSEException;
import de.webprojekt.conf.auth.jwt.JWTLoginData;
import de.webprojekt.conf.auth.jwt.JWTUtil;
import de.webprojekt.models.User;
import de.webprojekt.repository.UserRepository;
import de.webprojekt.utilities.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@Transactional
@RestController
@RequestMapping(path = "rest/auth")
public class JWTREST {
    @Autowired
    private UserRepository userRepository;
    @PostMapping(path = "authenticate",
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createJWToken(@RequestBody JWTLoginData credentials) throws JOSEException, NoSuchAlgorithmException {

        // do some proper lookup
        final String user = credentials.getUsername();
        final byte[] pwd = Password.getSHA(credentials.getPassword());
        final byte[] p=userRepository.getUserById(user).getPassword();
        final String password1 =Password.toHexString(p);
        final String password2 =Password.toHexString(pwd);
        final String u=userRepository.getUserById(user).getUsername();
        credentials.setRole(userRepository.getUserById(user).getRole());

        if (u.equals(user)&&password1.equals(password2)) {
           return ResponseEntity.ok(JWTUtil.createJWToken(credentials));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}

