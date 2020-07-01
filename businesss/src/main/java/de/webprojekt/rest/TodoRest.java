package de.webprojekt.rest;

import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import de.webprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Transactional
@RestController
@RequestMapping(path="/todoRestApi")
public class TodoRest {
    @Autowired
    private TodoRepository noteRepository;

    @PostMapping(path="/notes")
    public @ResponseBody String addNewNote (
            @RequestParam Todo todo
            /*@RequestParam String content,
            @RequestParam User user,
            @RequestParam String  status,
            @RequestParam Date remindDate,
            @RequestParam Time remindTime,
            @RequestParam Date date*/) {

        /*Notes note = new Notes();
        note.setUser(user);
        note.setType(type);
        note.setNote(content);
        note.setStatus(status);
        note.setRemindDate(remindDate);
        note.setRemindDate(remindTime);
        note.setCreatedAt(date);
        note.setUserId(user.getId());
        note.setUpdatedAt(date);*/
        noteRepository.save(todo);
        return "Saved";
    }
   @GetMapping(path="/notes1/{1}")
    public @ResponseBody
   List<Todo> getAllNotes(@PathVariable User user) {
        return noteRepository.findAllTodoByUser(user);
    }

    @GetMapping("/notes/{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
   @PatchMapping ("/notes/{id}")
    public Todo setNote(@RequestBody Todo newNote, @PathVariable Long id) {
        return noteRepository.findById(id)
                .map(notes -> {
                    notes.setTitle(newNote.getTitle());
                    notes.setContent(newNote.getContent());
                    notes.setDeadline(newNote.getDeadline());
                    notes.setUser(newNote.getUser());
                    notes.setDone(newNote.isDone());
                    return noteRepository.save(notes);
                })
                .orElseGet(() -> {
                    newNote.setId(id);
                    return noteRepository.save(newNote);
                });
    }

    @DeleteMapping("/notes/{id}")
    void deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
    }

}