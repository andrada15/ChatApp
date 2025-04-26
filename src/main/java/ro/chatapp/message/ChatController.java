package ro.chatapp.message;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.chatapp.endtoendencryption.Encryption;
import ro.chatapp.entities.Message;
import ro.chatapp.entities.User;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private Encryption rsaEncryption;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/chat")
    public String showChatPage(HttpServletRequest request, Model model) {

        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmailAddress(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        String privateKey = user.getPrivateKey();
        List<Message> decryptedMessages = messageService.getAllMessages().stream().map(msg -> {
            try {
                String decrypted = rsaEncryption.decrypt(msg.getContent(), privateKey);
                return new Message(msg.getUser(), decrypted);
            } catch (Exception e) {
                return msg;
            }
        }).toList();

        model.addAttribute("messages", decryptedMessages);
        model.addAttribute("username", username);
        return "chat";
    }
    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam String user, @RequestParam String message) {

        User sender = userRepository.findByEmailAddress(user).orElse(null);
        if (sender == null) {
            return "redirect:/chat/";
        }

        String publicKey = sender.getPublicKey();
        try {
            String encryptedMessage = rsaEncryption.encrypt(message, publicKey);
            messageService.saveMessage(user, encryptedMessage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/chat";
    }
}
