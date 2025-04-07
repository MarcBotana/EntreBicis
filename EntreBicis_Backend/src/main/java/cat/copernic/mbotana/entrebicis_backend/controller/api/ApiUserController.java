package cat.copernic.mbotana.entrebicis_backend.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
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

    @GetMapping("/detail/{email}")
    public ResponseEntity<User> userDetail(@PathVariable String email, Model model) {

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

    @PutMapping("/update/{email}")
    public ResponseEntity<Void> putMethodName(@PathVariable String email, @RequestBody User user) {

        ResponseEntity<Void> response = null;

        try {
            if (email.isEmpty() || user == null) {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                apiUserLogic.updateUser(user);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
