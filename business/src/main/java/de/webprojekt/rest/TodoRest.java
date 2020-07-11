package de.webprojekt.rest;

import de.webprojekt.models.Todo;
import de.webprojekt.dto.NewTodo;
import de.webprojekt.models.User;
import de.webprojekt.repository.TodoRepository;
import de.webprojekt.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;



@Transactional
@RestController
@RequestMapping(path="/rest/auth/todos")
public class TodoRest {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="")
    public ResponseEntity<?> addNewNote (@RequestBody NewTodo newTodo) {
        final Subject subject = SecurityUtils.getSubject();
        final String name=subject.getPrincipal().toString();
        if(subject.hasRole("admin")||name.equals(newTodo.getUsername())){
            User user=userRepository.findById(newTodo.getUsername())
                    .orElseThrow(() -> new NotFoundException(newTodo.getUsername()));
            Todo todo=new Todo();
            todo.setTitle(newTodo.getTitle());
            todo.setDone(newTodo.isDone());
            todo.setContent(newTodo.getContent());
            todo.setDeadline(newTodo.getDeadline());
            todo.setCreatedAt(newTodo.getCreatedAt());
            todo.setCompletedAt(newTodo.getCompletedAt());
            todo.setUser(user);
            Todo response=todoRepository.save(todo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping(path="")
    public @ResponseBody
    ResponseEntity<?> getAllTodos() {
       final Subject subject = SecurityUtils.getSubject();
       if(subject.hasRole("admin")){
           Iterable<Todo> response=todoRepository.findAll();
           return new ResponseEntity<>(response, HttpStatus.OK);
       }
       else{
           Iterable<Todo> response=todoRepository.findAllTodoByUser(subject.getPrincipal().toString());
           return new ResponseEntity<>(response, HttpStatus.OK);
       }

    }

    @GetMapping(path="my")
    public @ResponseBody
    ResponseEntity<?> getAllMyTodos() {
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin")){
            Iterable<Todo> response=todoRepository.findAllTodoByUser(subject.getPrincipal().toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Long id) {
        final Subject subject = SecurityUtils.getSubject();
        String name=subject.getPrincipal().toString();
        if(subject.hasRole("admin")) {
            Todo response=todoRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            Todo response=todoRepository.findByIdIfUser(id,name);
            if(response!=null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

    }

    @PutMapping ("/{id}")
    public Optional<Todo> partialsTodo(@RequestBody NewTodo newTodo, @PathVariable Long id) {
        SecurityUtils.getSubject().checkRole("admin");
        return todoRepository.findById(id)
                .map(notes -> {
                                if (newTodo.getTitle() != null) {
                                    notes.setTitle(newTodo.getTitle());
                                }
                                if (newTodo.getContent() != null) {
                                    notes.setContent(newTodo.getContent());
                                }
                                if (newTodo.getDeadline() != null) {
                                    notes.setDeadline(newTodo.getDeadline());
                                }
                                if (newTodo.getUsername() != null) {
                                    User user = userRepository.findById(newTodo.getUsername())
                                            .orElseThrow(() -> new NotFoundException(newTodo.getUsername()));
                                    notes.setUser(user);
                                }
                    notes.setDone(newTodo.isDone());
                    return todoRepository.save(notes);
                });

    }

   @PatchMapping ("/{id}")
    public ResponseEntity<?> setNote(@RequestBody NewTodo newTodo, @PathVariable Long id) {
       final Subject subject = SecurityUtils.getSubject();
       String name=subject.getPrincipal().toString();
       if(subject.hasRole("admin")) {
       Optional<Todo> response=todoRepository.findById(id).map(notes -> {
                        if(newTodo.getTitle()!=null) {
                            notes.setTitle(newTodo.getTitle());
                        }
                        if (newTodo.getContent()!=null){
                        notes.setContent(newTodo.getContent());
                        }
                        if(newTodo.getDeadline()!=null) {
                            notes.setDeadline(newTodo.getDeadline());
                        }
                        if(newTodo.getUsername()!=null) {
                           User user=userRepository.findById(newTodo.getUsername())
                                   .orElseThrow(() -> new NotFoundException(newTodo.getUsername()));
                            notes.setUser(user);
                        }
                       notes.setDone(newTodo.isDone());
                    return todoRepository.save(notes);
                });
           return new ResponseEntity<>(response, HttpStatus.OK);
       }else{
           Todo response=todoRepository.findByIdIfUser(id,name);
           if(response!=null){
               if(newTodo.getTitle()!=null){
                   todoRepository.UpdateTitelByIdIfUser(id,name,newTodo.getTitle());
               }
               if(newTodo.getContent()!=null) {
                   todoRepository.UpdateContentByIdIfUser(id,name,newTodo.getContent());
               }
               if(newTodo.isDone()) {
                   todoRepository.UpdateIsDoneByIdIfUser(id,name,true);
               }
               if(!newTodo.isDone()) {
                   todoRepository.UpdateIsDoneByIdIfUser(id,name,false);
               }
               if(newTodo.getDeadline()!=null) {
                   todoRepository.UpdateDeadlineByIdIfUser(id,name,newTodo.getDeadline());
               }
               if(newTodo.getCompletedAt()!=null) {
                   todoRepository.UpdateCompletedAtByIdIfUser(id,name,newTodo.getCompletedAt());
               }
               return new ResponseEntity<>("Daten Aktualisiert", HttpStatus.OK);
           }else{
               return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
           }
       }

    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteNote(@PathVariable Long id) {
        final Subject subject = SecurityUtils.getSubject();
        String name=subject.getPrincipal().toString();
        if(subject.hasRole("admin")) {
            todoRepository.deleteById(id);
            return new ResponseEntity<>("Loeschung erfolgreich abgeschlossen", HttpStatus.OK);
        }else{
            Todo response=todoRepository.findByIdIfUser(id,name);
            if(response!=null){
                todoRepository.deleteByIdIfUser(id,name);
                return new ResponseEntity<>("Loeschung erfolgreich abgeschlossen", HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        }

    }

}
