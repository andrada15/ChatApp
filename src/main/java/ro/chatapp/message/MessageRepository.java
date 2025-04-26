package ro.chatapp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.chatapp.entities.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findTop50ByOrderByTimestampDesc();
}
