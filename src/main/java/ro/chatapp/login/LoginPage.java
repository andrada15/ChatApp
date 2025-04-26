package ro.chatapp.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.chatapp.entities.User;

@Controller
public class LoginPage {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
        if (request.getAttribute("errorMessage") != null) {
            model.addAttribute("errorMessage", request.getAttribute("errorMessage"));
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String name, @RequestParam("password") String password, HttpServletRequest request, Model model) {
        if (name == null || name.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter a valid username/password");
            return "login";
        }

        if (loginService.validateLogin(name, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", name);
            return "redirect:/chat";
        }
        else {
            request.setAttribute("errorMessage", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpServletRequest request, Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("username") String name, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        User user = new User(name, email, password);
        boolean registered = loginService.registerUser(user, confirmPassword);

        if (!registered) {
            model.addAttribute("errorMessage", "Username or password does not match");
            return "register";
        }

        return "redirect:/login";
    }

}
