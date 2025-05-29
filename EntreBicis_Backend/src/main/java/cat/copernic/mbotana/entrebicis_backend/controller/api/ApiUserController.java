package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.Token;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.TokenLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserLogic apiUserLogic;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenLogic tokenLogic;

    @GetMapping("/getUserEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {

        ResponseEntity<User> response = null;

        User user = new User();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                user = apiUserLogic.getUserByEmail(email);
                response = new ResponseEntity<>(user, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {

        ResponseEntity<Void> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (user == null) {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!apiUserLogic.existUserByEmail(user.getEmail())) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                User userDB = apiUserLogic.getUserByEmail(user.getEmail());
                user.setReservations(userDB.getReservations());
                apiUserLogic.updateUser(user);
                response = new ResponseEntity<>(headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Void> updateUserPassword(@RequestBody User user) {

        ResponseEntity<Void> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (user == null) {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!apiUserLogic.existUserByEmail(user.getEmail())) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                User userDB = apiUserLogic.getUserByEmail(user.getEmail());
                userDB.setPassword(passwordEncoder.encode(user.getPassword()));
                userDB.setIsPasswordChanged(true);
                deleteToken(user);
                apiUserLogic.updateUser(userDB);
                response = new ResponseEntity<>(headers, HttpStatus.OK);
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
