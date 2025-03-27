package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class WebLoginController {

    @Autowired
    UserLogic userLogic;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {

        if (error != null) {
            model.addAttribute("error", "Credencials incorrectes!");
        }
        return "login";
    }

    @PostMapping("/user")
    public String loginSubmit(@RequestParam("email") String email, @RequestParam("password") String password) {

        try {
            User user = userLogic.getUserByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                return "redirect:/login?error=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/user/list";
    }

     @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }

    
    

}
