package de.webprojekt.repository;

import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT u FROM Todo u WHERE u.user = :x")
    List<Todo> findAllTodoByUser(@Param("x") User user);
}
