package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import cat.copernic.mbotana.entrebicis_backend.config.DataFormat;
import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.Token;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.UserState;
import cat.copernic.mbotana.entrebicis_backend.logic.SendEmailLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.TokenLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class WebUserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserLogic webUserLogic;

    @Autowired
    private TokenLogic tokenLogic;

    @Autowired
    private SendEmailLogic sendEmailLogic;

    @GetMapping("/create")
    public String createUserPage(Model model, @ModelAttribute("exceptionError") String exceptionError) {

        model.addAttribute("roleList", Role.values());
        model.addAttribute("userState", UserState.values());

        if (!model.containsAttribute("user")) {
            User user = new User();
            user.setPassword(generatePassword());
            model.addAttribute("user", user);
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "user_create";
    }

    @PostMapping("/create/new")
    public String createUser(@Valid @ModelAttribute("user") User newUser, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {

            if (webUserLogic.existUserByEmail(newUser.getEmail())) {
                result.rejectValue("email", "error.user", ErrorMessage.EMAIL_EXIST);
            }

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("user", newUser);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                return "redirect:/user/create";

            } else {
                newUser.setTotalPoints(0.0);
                newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                newUser.setIsRouteStarted(false);
                newUser.setIsPasswordChanged(false);
                newUser.setUserState(UserState.ACTIVE);
                webUserLogic.saveUser(newUser);
            }

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        }

        return "redirect:/user/list";
    }

    @GetMapping("/list")
    public String listUsersPage(@RequestParam(required = false) String sort,
            @RequestParam(required = false) String search, Model model) {

        List<User> allUsers = new ArrayList<>();

        try {
            allUsers = webUserLogic.getAllUsers();

            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "email" -> allUsers.sort(Comparator.comparing(User::getEmail));
                    case "name" -> allUsers.sort(Comparator.comparing(User::getName));
                    case "BIKER" ->
                        allUsers = allUsers.stream().filter(user -> user.getRole().equals(Role.BIKER)).toList();
                    case "ADMIN" ->
                        allUsers = allUsers.stream().filter(user -> user.getRole().equals(Role.ADMIN)).toList();
                }
            }

            if (search != null && !search.isBlank()) {
                allUsers = allUsers.stream().filter(user -> user.getEmail().toLowerCase().contains(search.toLowerCase())
                        || user.getName().toLowerCase().contains(search.toLowerCase())).toList();
            }

        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("allUsers", allUsers);

        return "users_list";
    }

    @GetMapping("/detail")
    public String userDetailPage(@RequestParam(required = true) String email, Model model) {

        User user = new User();

        try {
            user = webUserLogic.getUserByEmail(email);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("user", user);

        return "user_detail";
    }

    @GetMapping("/update")
    public String updateUserPage(@RequestParam(required = true) String email, Model model,
            @ModelAttribute("exceptionError") String exceptionError) {

        model.addAttribute("roleList", Role.values());
        model.addAttribute("userState", UserState.values());

        User user = new User();

        try {
            user = webUserLogic.getUserByEmail(email);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "redirect:/user/list";
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "user_update";
    }

    @PostMapping("/update/new")
    public String updateUser(@Valid @ModelAttribute("user") User newUser, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("user", newUser);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                return "redirect:/user/update?email=" + URLEncoder.encode(newUser.getEmail(), StandardCharsets.UTF_8);

            } else {
                webUserLogic.updateUser(newUser);
            }

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        }

        return "redirect:/user/list";
    }

    @ModelAttribute("showPasswordForm")
    public Boolean getShowPasswordForm(
            @RequestParam(name = "showPasswordForm", required = false) Boolean showPasswordForm) {
        return showPasswordForm != null ? showPasswordForm : false;
    }

    @GetMapping("/update/password")
    public String userPasswordPage(@RequestParam(required = true) String email, Model model,
            @ModelAttribute("exceptionError") String exceptionError) {

        User user = new User();

        model.addAttribute("tokenLength", DataFormat.MAX_TOKEN_LENGTH);

        try {
            user = webUserLogic.getUserByEmail(email);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("user", user);

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "user_password";
    }

    @PostMapping("/update/password/new")
    public String updateUserPasswordPage(@Valid @ModelAttribute("user") User newUser,
            @RequestParam("token") String tokenCode, @RequestParam("repPassword") String repPassword,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {

            Boolean isValid = false;

            Optional<Token> tokenDB = tokenLogic.getByToken(tokenCode);

            if (tokenDB.isPresent()) {
                if (tokenDB.get().getUser().getEmail().equals(newUser.getEmail())) {
                    if (!tokenDB.get().isExpired()) {
                        isValid = true;
                    } else {
                        result.rejectValue("token", "errorToken", ErrorMessage.TOKEN_EXPIRED);
                    }
                } else {
                    result.rejectValue("token", "errorToken", ErrorMessage.TOKEN_NOT_FOUND);
                }
            } else {
                result.rejectValue("token", "errorToken", ErrorMessage.TOKEN_NOT_FOUND);
            }

            if (repPassword.equals(newUser.getPassword())) {
                isValid = true;
            } else {
                result.rejectValue("repPassword", "errorRepPassword", ErrorMessage.PASS_NOT_MATCH);
            }

            if (result.hasErrors()) {
                if (result.hasFieldErrors("token")) {
                    Optional.ofNullable(result.getFieldError("token"))
                            .ifPresent(error -> redirectAttributes.addFlashAttribute("errorToken",
                                    error.getDefaultMessage()));
                }
                if (result.hasFieldErrors("repPassword")) {
                    Optional.ofNullable(result.getFieldError("repPassword"))
                            .ifPresent(error -> redirectAttributes.addFlashAttribute("errorRepPassword",
                                    error.getDefaultMessage()));
                }
                redirectAttributes.addFlashAttribute("user", newUser);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                redirectAttributes.addFlashAttribute("showPasswordForm", true);
                return "redirect:/user/update/password?email="
                        + URLEncoder.encode(newUser.getEmail(), StandardCharsets.UTF_8);

            } else if (isValid) {
                newUser.setIsPasswordChanged(true);
                webUserLogic.updateUser(newUser);
            }

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user/update/password?email="
                    + URLEncoder.encode(newUser.getEmail(), StandardCharsets.UTF_8);
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user/update/password?email="
                    + URLEncoder.encode(newUser.getEmail(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user/update/password?email="
                    + URLEncoder.encode(newUser.getEmail(), StandardCharsets.UTF_8);
        }

        return "redirect:/user/detail?email=" + URLEncoder.encode(newUser.getEmail(), StandardCharsets.UTF_8);
    }

    @PostMapping("/send/email")
    public String sendEmailPassword(@RequestParam("email") String email, Model model,
            RedirectAttributes redirectAttributes) {

        User user = new User();

        try {

            user = webUserLogic.getUserByEmail(email);

            deleteToken(user);

            String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            Token resetToken = new Token(token, user);

            tokenLogic.saveToken(resetToken);

            sendEmailLogic.sendEmailPassword(user.getEmail(), token);

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user/update/password?email="
                    + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user/update/password?email="
                    + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user/update/password?email="
                    + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        }

        redirectAttributes.addFlashAttribute("showPasswordForm", true);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/user/update/password?email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
    }

    private static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(DataFormat.LOWERCASE.charAt(random.nextInt((DataFormat.LOWERCASE.length()))));
        password.append(DataFormat.UPPERCASE.charAt(random.nextInt(DataFormat.UPPERCASE.length())));
        password.append(DataFormat.DIGITS.charAt(random.nextInt(DataFormat.DIGITS.length())));
        password.append(DataFormat.SPECIAL_CHAR.charAt(random.nextInt(DataFormat.SPECIAL_CHAR.length())));

        String allCharacters = DataFormat.LOWERCASE + DataFormat.UPPERCASE + DataFormat.DIGITS
                + DataFormat.SPECIAL_CHAR;
        while (password.length() < DataFormat.MIN_LENGTH) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return password.toString();
    }

    public void deleteToken(User user) {
        Optional<Token> token = tokenLogic.getByUser(user);
        token.ifPresent(tokenLogic::deleteToken);
    }

}
