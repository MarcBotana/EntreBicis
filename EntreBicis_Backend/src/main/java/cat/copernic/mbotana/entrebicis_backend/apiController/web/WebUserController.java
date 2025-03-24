package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import cat.copernic.mbotana.entrebicis_backend.config.DataFormat;
import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.UserState;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class WebUserController {

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @Autowired
    private UserLogic webUserLogic;

    @GetMapping("/create")
    public String createUserPage(Model model, @ModelAttribute("exceptionError") String exceptionError) {

        model.addAttribute("roleList", Role.values());
        model.addAttribute("userState", UserState.values());

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "user_create";
    }

    @PostMapping("/create/new")
    public String createUser(@Valid @ModelAttribute("user") User newUser, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {

        try {

            if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
                if (!newUser.getPassword().matches(DataFormat.USR_PASS_REGEX)) {
                    result.rejectValue("password", "password.format.error", ErrorMessage.PASSWORD_FORMAT + ErrorMessage.PASSWORD_FORMAT_INFO);
                }
            }
            

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("user", newUser);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                return "redirect:/user/create";
            } else {
                //newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                webUserLogic.saveUser(newUser);
            }        

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        }

        return "redirect:/users";
    }

    @GetMapping("/list")
    public String getAllUsers(Model model) {

        List<User> allUsers = new ArrayList<>();

        try {
            allUsers = webUserLogic.getAllUsers();
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("userList", allUsers);

        return "users_list";
    }

    @GetMapping("/detail")
    public String getUserByEmail(@RequestBody String email, Model model) {

        User user = new User();

        try {
            user = webUserLogic.getUserByEmail(email);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("user", user);

        return "user_detail";
    }
    
}
