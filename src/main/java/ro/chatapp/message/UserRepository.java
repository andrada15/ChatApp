package ro.chatapp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.chatapp.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAddress(String email);
    Optional<User> findByName(String username);
}
