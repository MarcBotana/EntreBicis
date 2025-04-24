package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.GpsPoint;
import cat.copernic.mbotana.entrebicis_backend.entity.Route;
import cat.copernic.mbotana.entrebicis_backend.repository.GpsPointRepository;

@Service
public class GpsPointLogic {

    @Autowired
    GpsPointRepository gpsPointRepository;

    public GpsPoint saveGpsPoint(GpsPoint gpsPoints) throws Exception {
        return gpsPointRepository.save(gpsPoints);
    }

    public GpsPoint getGpsPointsById(Long id) throws Exception {
        return gpsPointRepository.findById(id).get();
    }

    public List<GpsPoint> getAllRouteGpsPoints(Route route) throws Exception {
        return gpsPointRepository.findGpsPointByRoute(route);
    }

    public void updateGpsPoint(GpsPoint gpsPoint) throws Exception {
        gpsPointRepository.save(gpsPoint);
    }








}
