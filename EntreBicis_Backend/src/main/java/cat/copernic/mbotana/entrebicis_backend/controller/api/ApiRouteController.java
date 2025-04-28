package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.GpsPoint;
import cat.copernic.mbotana.entrebicis_backend.entity.Route;
import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.logic.GpsPointLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.RouteLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/route")
public class ApiRouteController {

    @Autowired
    private RouteLogic apiRouteLogic;

    @Autowired
    private UserLogic apiUserLogic;

    @Autowired
    private SystemParamsLogic apiSystemParamsLogic;

    @Autowired
    private GpsPointLogic apiGpsPointLogic;

    @PostMapping("/create/{email}")
    public ResponseEntity<Void> createRoute(@PathVariable String email, @RequestBody Route route) {

        ResponseEntity<Void> response = null;

        Route newRoute = null;

        List<GpsPoint> newGpsPoints = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                SystemParams systemParams = apiSystemParamsLogic.getSystemParamsById(1L);
                int systemMaxVelocity = systemParams.getMaxVelocity();
                Double systemPointsConversion = systemParams.getPointsConversion();

                User user = apiUserLogic.getUserByEmail(email);

                newRoute = new Route(
                        null,
                        route.getIsValidate(),
                        systemPointsConversion * route.getTotalRouteDistance(),
                        route.getTotalRouteDistance(),
                        route.getTotalRouteTime(),
                        route.getMaxRouteVelocity(),
                        route.getAvgRouteVelocity(),
                        null,
                        user);

                Route savedRoute = apiRouteLogic.saveRoute(newRoute);

                for (GpsPoint gpsPoint : route.getGpsPoints()) {
                    gpsPoint.setRoute(savedRoute);
                    if (gpsPoint.getSpeed() >= systemMaxVelocity) {
                        gpsPoint.setIsValid(false);
                    }
                    newGpsPoints.add(apiGpsPointLogic.saveGpsPoint(gpsPoint));
                }

                savedRoute.setGpsPoints(newGpsPoints);

                user.setTotalPoints(newRoute.getTotalRutePoints());

                apiUserLogic.updateUser(user);

                apiRouteLogic.updateRoute(savedRoute);

                response = new ResponseEntity<>(headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<Route>> getAllRoutesList() {

        ResponseEntity<List<Route>> response = null;

        List<Route> allRoutes = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            allRoutes = apiRouteLogic.getAllRoutes();
            response = new ResponseEntity<>(allRoutes, headers, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/list/{email}")
    public ResponseEntity<List<Route>> getUserRoutesList(@PathVariable String email) {

        ResponseEntity<List<Route>> response = null;

        List<Route> allUserRoutes = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                User user = apiUserLogic.getUserByEmail(email);

                allUserRoutes = apiRouteLogic.getAllUserRoutes(user);
                response = new ResponseEntity<>(allUserRoutes, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
