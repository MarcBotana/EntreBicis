package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.Route;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.repository.RouteRepository;

@Service
public class RouteLogic {

    @Autowired
    private RouteRepository routeRepository;

    public Route saveRoute(Route route) throws Exception {
        return routeRepository.save(route);
    }

    public Route getRouteById(Long id) throws Exception {
        return routeRepository.findById(id).get();
    }

    public List<Route> getAllRoutes() throws Exception {
        return routeRepository.findAll();
    }

    public List<Route> getAllUserRoutes(User user) throws Exception {
        return routeRepository.findRouteByUser(user);
    }
    public void updateRoute(Route route) throws Exception {
        routeRepository.save(route);
    }

    public Boolean existRouteById(Long id) throws Exception {
        return routeRepository.existsById(id);
    }

}
