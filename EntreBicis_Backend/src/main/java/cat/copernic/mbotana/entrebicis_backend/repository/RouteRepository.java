package cat.copernic.mbotana.entrebicis_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.mbotana.entrebicis_backend.entity.Route;
import cat.copernic.mbotana.entrebicis_backend.entity.User;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findRouteByUser(User user);

}
