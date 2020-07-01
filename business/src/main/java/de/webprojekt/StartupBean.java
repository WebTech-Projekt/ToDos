package de.webprojekt;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Transactional
public class StartupBean implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private EntityManager entityManager;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    final User firstUserItem = this.entityManager.find(User.class,"Muster");
    final Todo firstNoteItem = this.entityManager.find(Todo.class, 1L);
    // only initialize once
    Date dt = new Date();

    if (firstUserItem == null) {
      final User user = new User();
      user.setUserName("MusterName");
      user.setDisplayName("Muster");
      user.setAdmin(true);
      this.entityManager.persist(user);
      if(firstNoteItem==null){
        final Todo todo=new Todo();
        todo.setTitle("todo1");
        todo.setContent("shopping");
        todo.setCreatedAt(dt);
        todo.setUser(user);
        todo.setDeadline(dt);
        this.entityManager.persist(todo);
      }

    }

  }
}