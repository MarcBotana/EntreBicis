package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.repository.UserRepository;

@Service
public class UserLogic {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) throws Exception {
        userRepository.save(user);
    }

    @Transactional
    public User getUserByEmail(String email) throws Exception {
        return userRepository.findById(email).get();
    }

    public List<User> getAllUsers() throws Exception {
        return userRepository.findAll();
    }

    public void updateUser(User user) throws Exception {
        userRepository.save(user);
    }

    public Boolean existUserByEmail(String email) throws Exception {
        return userRepository.existsById(email); 
    }
}
