package de.webprojekt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Todo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String title;
    @NotNull
    @Column(length = 70)
    private String content;
    private boolean isDone;
    private Date deadline;
    private Date createdAt;
    private Date completedAt;
   
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="user", nullable=false)
    @JsonIgnore
    private User user;
}
