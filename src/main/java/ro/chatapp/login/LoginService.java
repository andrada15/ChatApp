package ro.chatapp.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.chatapp.endtoendencryption.Encryption;
import ro.chatapp.entities.User;
import ro.chatapp.message.UserRepository;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    Logger log = Logger.getLogger(LoginService.class.getName());

    public boolean validateLogin(String email, String password) {
        Optional<User> user = userRepository.findByEmailAddress(email);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public boolean registerUser(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            return false;
        }
        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            return false;
        }

        try {
        Encryption keypair = new Encryption();
        keypair.generateKeyPair();
        PublicKey publicKey = keypair.getPublicKey();
        PrivateKey privateKey = keypair.getPrivateKey();

        user.setPublicKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        user.setPrivateKey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        userRepository.save(user);

        return true;
    } catch (Exception e) {
        log.warning("Failed to register user: " + e.getMessage());
        return false;
        }
    }

    private boolean validateEmailAddress(String emailAddress) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)\\.(\\w{2,})$").matcher(emailAddress).matches();
    }
}
