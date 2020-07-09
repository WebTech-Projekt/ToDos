package de.webprojekt.repository;

import org.springframework.transaction.annotation.Transactional;
import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query(value = "SELECT * FROM todo WHERE user = ?1", nativeQuery = true)
    List<Todo> findAllTodoByUser(String username);

    @Query(value = "SELECT * FROM todo WHERE id=?1 AND user = ?2", nativeQuery = true)
    Todo findByIdIfUser(Long id,String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM todo  WHERE id=?1 AND user=?2", nativeQuery = true)
    void deleteByIdIfUser(Long id,String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo SET title=?3  WHERE id=?1 AND user=?2", nativeQuery = true)
    void UpdateTitelByIdIfUser(Long id,String username,String title);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo SET content=?3  WHERE id=?1 AND user=?2", nativeQuery = true)
    void UpdateContentByIdIfUser(Long id,String username,String content);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo SET deadline=?3  WHERE id=?1 AND user=?2", nativeQuery = true)
    void UpdateDeadlineByIdIfUser(Long id, String username, Date date);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo SET is_done=?3  WHERE id=?1 AND user=?2", nativeQuery = true)
    void UpdateIsDoneByIdIfUser(Long id,String username,boolean isDone);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo SET completed_at=?3  WHERE id=?1 AND user=?2", nativeQuery = true)
    void UpdateCompletedAtByIdIfUser(Long id,String username,Date date);

}
