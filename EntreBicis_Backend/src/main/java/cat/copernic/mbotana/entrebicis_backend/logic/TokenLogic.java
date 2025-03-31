package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.Token;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.repository.TokenRepo;


@Service
public class TokenLogic {

    @Autowired
    private TokenRepo tokenRepo;
    
    public void saveToken(Token token) {
        tokenRepo.save(token);
    }

    public void deleteToken(Token token) {
        tokenRepo.delete(token);
    }

    public Optional<Token> getByUser(User user) {
        return tokenRepo.findByUser(user);
    }

    public Optional<Token> getByToken(String token) {
        return tokenRepo.findByTokenCode(token);
    }
    
}
