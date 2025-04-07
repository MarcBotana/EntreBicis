package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.Token;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.SendEmailLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.TokenLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

@RestController
@RequestMapping("/api/login")
public class ApiLoginController {

    @Autowired
    private UserLogic apiUserLogic;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private TokenLogic tokenLogic;

    @Autowired
    private SendEmailLogic sendEmailLogic;

    @GetMapping("/validate/{email}/{password}")
    public ResponseEntity<User> validateUser(@PathVariable String email, @PathVariable String password) {
        
        ResponseEntity<User> response = null;

        User user = new User();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                user = apiUserLogic.getUserByEmail(email);
                if (user.getIsPasswordChanged()) {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        response = new ResponseEntity<>(user, headers, HttpStatus.OK);
                    } else {
                        response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    response = new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping("/sendEmail/{email}")
    public ResponseEntity<Void> sendEmail(@PathVariable String email) {

        ResponseEntity<Void> response = null;

        User user = new User();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                user = apiUserLogic.getUserByEmail(email);

                deleteToken(user);

                String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                
                Token resetToken = new Token(token, user);

                tokenLogic.saveToken(resetToken);

                sendEmailLogic.sendEmailPassword(email, token);

                response = new ResponseEntity<>(HttpStatus.OK);
            }

        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/validateToken/{token}/{email}")
    public ResponseEntity<Boolean> validateToken(@PathVariable String token, @PathVariable String email) {

        ResponseEntity<Boolean> response = null;

        Boolean isValid = false;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            Optional<Token> tokenDB = tokenLogic.getByToken(token);

            if (tokenDB.isPresent()) {
                if (tokenDB.get().getUser().getEmail().equals(email)) {
                    if (!tokenDB.get().isExpired()) {
                        isValid = true;
                        response = new ResponseEntity<>(isValid, headers, HttpStatus.OK);
                    } else {
                        response = new ResponseEntity<>(isValid, headers, HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    response = new ResponseEntity<>(isValid, headers, HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>(isValid, headers, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public void deleteToken(User user) {
        Optional<Token> token = tokenLogic.getByUser(user);
        token.ifPresent(tokenLogic::deleteToken);
    }

}
