package de.webprojekt.rest;

import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import de.webprojekt.repository.TodoRepository;
import de.webprojekt.repository.UserRepository;
import de.webprojekt.rest.NotFoundException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;


@Transactional
@RestController
@RequestMapping(path="/rest/auth/users")
public class UserRest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TodoRepository noteRepository;

    @PostMapping(path="",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> newUser(@RequestBody User user) {
        SecurityUtils.getSubject().checkRole("admin");
         userRepository.save(user);
        return ResponseEntity.ok("resource updated");
    }

    @GetMapping(path="")
    public @ResponseBody Iterable<User> getAllUsers() {
        SecurityUtils.getSubject().checkRole("admin");
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("username") String username) {
        SecurityUtils.getSubject().checkRole("admin");
        return userRepository.findById(username)
                .orElseThrow(() -> new NotFoundException(username));
    }
    @PutMapping(path="/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User setUser(@RequestBody User newUser, @PathVariable("username") String username) {
        SecurityUtils.getSubject().checkRole("admin");
        return userRepository.findById(username)
                .map(users -> {
                    users.setRole(newUser.getRole());
                    users.setPassword(newUser.getPassword());
                    users.setTodo(newUser.getTodo());
                    return userRepository.save(users);
                })
                .orElseThrow(() -> new NotFoundException(username))
                ;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> partialUpdateGeneric(
            @RequestBody User user,
            @PathVariable("username") String username) {
        SecurityUtils.getSubject().checkRole("admin");
        userRepository.findById(username).map(users->{
            if(user.getPassword()!=null){
            users.setPassword(user.getPassword());
            }
            if(user.getRole()!=null){
                users.setRole(user.getRole());
            }
            return userRepository.save(users);
        });
        return ResponseEntity.ok("Die Aktualisierung ist erfolgreich abgeschlossen");
    }
    @DeleteMapping("/{username}")
    public String deleteUser(@PathVariable("username") String username) {
        SecurityUtils.getSubject().checkRole("admin");
       User user=getUserById(username);
        for (Todo todo : user.getTodo()){
            if(user.equals(todo.getUser())){
            noteRepository.delete(todo);
            }
        }
        userRepository.deleteById(username);
        return "Loeschung erfolgreich abgeschlossen";
    }

}