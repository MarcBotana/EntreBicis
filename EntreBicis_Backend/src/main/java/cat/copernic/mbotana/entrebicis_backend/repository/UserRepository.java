package cat.copernic.mbotana.entrebicis_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.mbotana.entrebicis_backend.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

}
