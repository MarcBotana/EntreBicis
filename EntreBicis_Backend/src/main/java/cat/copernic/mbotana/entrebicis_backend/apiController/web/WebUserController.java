package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class WebUserController {

    @Autowired
    private UserLogic webUserLogic;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/create")
    public String createUserPage(Model model, @ModelAttribute("exceptionError") String exceptionError, @ModelAttribute("newUser") User newUser) {

        model.addAttribute("roleList", Role.values());
        model.addAttribute("userState", UserState.values());
        model.addAttribute("user", new User());

        if (model.containsAttribute("newUser")) {
            model.addAttribute("user", newUser);
        } else {
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

            if (!newUser.getPassword().matches(DataFormat.USR_PASS_REGEX)) {
                result.rejectValue("password", "password.format.error", ErrorMessage.PASSWORD_FORMAT + ErrorMessage.PASSWORD_FORMAT_INFO);
            }

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("BindingResult.newUser", result);
                redirectAttributes.addFlashAttribute("newUser", newUser);
                return "redirect:/user_create";
            } else {
                newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                webUserLogic.saveUser(newUser);
            }        

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user_create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user_create";
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user_create";
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
