package de.webprojekt.rest;

import de.webprojekt.models.User;
import de.webprojekt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequestMapping(path="/todoRestApi")
public class UserRest {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/users")
    public @ResponseBody String addNewUser (
            @RequestParam String userName,
            @RequestParam String lastName,
            @RequestParam String role,
            @RequestParam boolean active,
            @RequestParam String email) {

        User user = new User();
        user.setUserName(userName);
        user.setDisplayName(lastName);
        user.setAdmin(true);
        userRepository.save(user);
        return "Saved";
    }

    @GetMapping(path="/users")
    public @ResponseBody Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }
    @PutMapping("/users/{id}")
    public User setUser(@RequestBody User newUser, @PathVariable String id) {

        return userRepository.findById(id)
                .map(users -> {
                    users.setUserName(newUser.getUserName());
                    users.setAdmin(newUser.isAdmin());
                    return userRepository.save(users);
                })
                .orElseGet(() -> {
                    newUser.setDisplayName(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
    }

}