package de.webprojekt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
public class User {
  @Id
  @Column(length = 30)
  private String username;
  @Column(length = 70)
  @JsonIgnore
  private byte[] password;
  private String role;
  //private Collection<String> permissions;
  @OneToMany(mappedBy = "user",fetch= FetchType.LAZY)
  private Collection<Todo> todo;

  public User() {
    this.username = "Test";
  }

  public User(String username, byte[] password) {
    this.username = username;
    this.password = password;
  }

  public User(String username, byte[] password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public User(String username, byte[] password, String role, Collection<Todo> todo) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.todo = todo;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public byte[] getPassword() {
    return password;
  }

  public void setPassword(byte[] password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  /*public Collection<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }*/

  public Collection<Todo> getTodo() {
    return todo;
  }

  public void setTodo(Collection<Todo> todo) {
    this.todo = todo;
  }
}
