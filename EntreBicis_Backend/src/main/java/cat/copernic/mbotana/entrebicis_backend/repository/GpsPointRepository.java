package cat.copernic.mbotana.entrebicis_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.mbotana.entrebicis_backend.entity.GpsPoint;
import cat.copernic.mbotana.entrebicis_backend.entity.Route;

public interface GpsPointRepository extends JpaRepository<GpsPoint, Long> {

            List<GpsPoint> findGpsPointByRoute(Route route);

}
