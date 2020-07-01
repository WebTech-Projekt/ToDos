package de.webprojekt.rest;

import de.webprojekt.models.Todo;
import de.webprojekt.dto.NewTodo;
import de.webprojekt.models.User;
import de.webprojekt.repository.TodoRepository;
import de.webprojekt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;



@Transactional
@RestController
@RequestMapping(path="/todos")
public class TodoRest {
    @Autowired
    private TodoRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="")
    public Todo addNewNote (@RequestBody NewTodo newTodo) {
        System.out.println(newTodo.getDisplayName());

        try{
            User user=userRepository.findById(newTodo.getDisplayName())
                    .orElseThrow(() -> new NotFoundException(newTodo.getDisplayName()));
            Todo todo=new Todo();
            todo.setTitle(newTodo.getTitle());
            todo.setDone(newTodo.isDone());
            todo.setContent(newTodo.getContent());
            todo.setDeadline(newTodo.getDeadline());
            todo.setCreatedAt(newTodo.getCreatedAt());
            todo.setCompletedAt(newTodo.getCompletedAt());
            todo.setUser(user);
            return  noteRepository.save(todo);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return  null;
    }

   @GetMapping(path="")
    public @ResponseBody
   Iterable<Todo> getAllNotes() {
        return noteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping ("/{id}")
    public Optional<Todo> partialsTodo(@RequestBody NewTodo newTodo, @PathVariable Long id) {
        return noteRepository.findById(id)
                .map(notes -> {
                    User user=userRepository.findById(newTodo.getDisplayName())
                            .orElseThrow(() -> new NotFoundException(newTodo.getDisplayName()));
                    if(newTodo.getTitle()!=null){
                    notes.setTitle(newTodo.getTitle());
                    }
                    if(newTodo.getContent()!=null){
                    notes.setContent(newTodo.getContent());
                    }
                    if(newTodo.getDeadline()!=null) {
                        notes.setDeadline(newTodo.getDeadline());
                    }
                    if(newTodo.getDisplayName()!=null) {
                        notes.setUser(user);
                    }
                    notes.setDone(newTodo.isDone());
                    return noteRepository.save(notes);
                });

    }

   @PatchMapping ("/{id}")
    public Optional<Todo> setNote(@RequestBody NewTodo newTodo, @PathVariable Long id) {
        return noteRepository.findById(id).map(notes -> {
                    User user=userRepository.findById(newTodo.getDisplayName())
                                .orElseThrow(() -> new NotFoundException(newTodo.getDisplayName()));
                    if(newTodo.getTitle()!=null) {
                        notes.setTitle(newTodo.getTitle());
                    }
                    if (newTodo.getContent()!=null){
                    notes.setContent(newTodo.getContent());
                    }
                    if(newTodo.getDeadline()!=null) {
                        notes.setDeadline(newTodo.getDeadline());
                    }
                    if(newTodo.getDisplayName()!=null) {
                        notes.setUser(user);
                    }
                     notes.setDone(newTodo.isDone());
                    return noteRepository.save(notes);
                });

    }

    @DeleteMapping("/{id}")
    String deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
        return "Loeschung erfolgreich abgeschlossen";
    }

}