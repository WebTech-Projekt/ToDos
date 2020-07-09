package de.webprojekt.auth;

import de.webprojekt.models.Todo;
import de.webprojekt.repository.TodoRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequestMapping(path = {"rest/auth"})
public class UserREST {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping(path ="/user", produces=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getProfile() {

        final Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(subject.getPrincipal().toString());
    }

}
