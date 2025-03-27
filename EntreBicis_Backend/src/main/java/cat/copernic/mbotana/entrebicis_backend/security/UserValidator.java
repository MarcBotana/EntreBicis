package cat.copernic.mbotana.entrebicis_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

@Service
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CustomUserDetails(user);
    }

}
