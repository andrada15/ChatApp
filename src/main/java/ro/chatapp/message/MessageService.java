package ro.chatapp.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.chatapp.entities.Message;
import ro.chatapp.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public Message saveMessage(String username, String content) {
        User user = (User) userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Message message = new Message();
        message.setUser(user);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findTop50ByOrderByTimestampDesc();
    }
}
