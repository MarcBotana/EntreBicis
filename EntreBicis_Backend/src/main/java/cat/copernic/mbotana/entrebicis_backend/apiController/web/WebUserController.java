package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.web.WebUserLogic;

@Controller
public class WebUserController {

    @Autowired
    private WebUserLogic webUserLogic;

    public void saveUser(User user) {
        webUserLogic.saveUser(user);
    }

}
