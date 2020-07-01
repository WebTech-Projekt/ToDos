package de.webprojekt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
public class User {
  @Id
  @Column(length = 30)
  private String displayName;
  @Column(length = 100)
  private String userName;
  @Column(length = 70)
  @JsonIgnore
  private byte[] passwordHash;
  private boolean isAdmin;
  @OneToMany(mappedBy = "user",fetch= FetchType.LAZY)
  private Collection<Todo> todo;

  public User() {
    this.displayName = "Test";
  }
  public User(String displayName) {
    this.displayName = displayName;
  }

  public User(String displayName, String userName, boolean isAdmin) {
    this.displayName = displayName;
    this.userName = userName;
    this.isAdmin = isAdmin;
  }

  public User(String displayName, String userName, byte[] passwordHash, boolean isAdmin, Collection<Todo> todo) {
    this.displayName = displayName;
    this.userName = userName;
    this.passwordHash = passwordHash;
    this.isAdmin = isAdmin;
    this.todo = todo;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public byte[] getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(byte[] passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  public Collection<Todo> getTodo() {
    return todo;
  }
  public Object[] getTodoObject() {
    return todo.toArray();
  }

  public void setTodo(Collection<Todo> todo) {
    this.todo = todo;
  }
}
