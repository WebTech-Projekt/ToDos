package de.webprojekt.repository;

import de.webprojekt.models.Todo;
import de.webprojekt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = :x")
    User getUserById(@Param("x") String username);
}
    