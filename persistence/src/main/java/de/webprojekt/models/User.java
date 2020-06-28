package de.webprojekt.models;

import javax.persistence.*;
import java.util.Collection;

@Entity // This tells Hibernate to make a table out of this class
public class User {
  @Column(length = 30)
  private String displayName;
  @Column(length = 100)
  private String userName;
  @Column(length = 70)
  private byte[] passwordHash;
  private boolean isAdmin;
  @OneToMany(mappedBy = "user",fetch= FetchType.LAZY)
  private Collection<Todo> todo;
}
