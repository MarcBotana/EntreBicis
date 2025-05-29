package cat.copernic.mbotana.entrebicis_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.copernic.mbotana.entrebicis_backend.entity.Token;
import cat.copernic.mbotana.entrebicis_backend.entity.User;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {

    Optional<Token> findByUser(User user);

    Optional<Token> findByTokenCode(String tokenCode);


}

