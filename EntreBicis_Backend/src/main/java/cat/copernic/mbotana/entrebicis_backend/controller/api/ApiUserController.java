package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
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

    @GetMapping("/list")
    public ResponseEntity<List<User>> userList(Model model) {
        
        ResponseEntity<List<User>> response = null;

        List<User> allUsers = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            allUsers = apiUserLogic.getAllUsers();

            response = new ResponseEntity<>(allUsers, headers, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @PutMapping("/update")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }


    
}
