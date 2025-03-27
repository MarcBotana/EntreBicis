package cat.copernic.mbotana.entrebicis_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

public class UserValidator implements UserDetailsService {

    @Autowired
    private UserLogic userLogic;

    public UserValidator(){
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = new User();
        
        try {
            user = userLogic.getUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("L'usuari no s'ha trobat!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return user;
    }

}
