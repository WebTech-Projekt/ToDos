package de.webprojekt;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import de.webprojekt.utilities.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

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
      final User user1 = new User();
      user.setUsername("Muster");
      user.setRole("admin");
      user1.setUsername("User1");
      user1.setRole("author");

      try {
        user.setPassword(Password.getSHA("Test"));
        user1.setPassword(Password.getSHA("Test1"));
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
      this.entityManager.persist(user);
      this.entityManager.persist(user1);
      if(firstNoteItem==null){
        final Todo todo=new Todo();
        final Todo todo1=new Todo();
        todo.setTitle("todo1");
        todo.setContent("shopping");
        todo.setCreatedAt(dt);
        todo.setUser(user);
        todo.setDeadline(dt);
        todo1.setTitle("todo2");
        todo1.setContent("work");
        todo1.setCreatedAt(dt);
        todo1.setUser(user1);
        todo1.setDeadline(dt);
        this.entityManager.persist(todo);
        this.entityManager.persist(todo1);
      }

    }

  }
}