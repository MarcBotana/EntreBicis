package cat.copernic.mbotana.entrebicis_backend.controller.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.mbotana.entrebicis_backend.entity.Route;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RouteState;
import cat.copernic.mbotana.entrebicis_backend.logic.RouteLogic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/route")
public class WebRouteController {

    @Autowired
    RouteLogic webRouteLogic;

    @GetMapping("/list")
    public String listRoutePage(@RequestParam(required = false) String sort,
            @RequestParam(required = false) String search, Model model) {

        List<Route> allRoutes = new ArrayList<>();

        try {

            allRoutes = webRouteLogic.getAllRoutes();
            
            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "routeDate":
                        allRoutes.sort(Comparator.comparing(route -> route.getRouteDate()));
                        break;
                    case "routeUser":
                        allRoutes.sort(Comparator.comparing(route -> route.getUser().getName()));
                        break;
                    case "VALIDATED":
                        allRoutes = allRoutes.stream()
                                .filter(route -> route.getRouteState()
                                        .equals(RouteState.VALIDATED))
                                .toList();
                        break;
                    case "NOT_VALIDATED":
                        allRoutes = allRoutes.stream()
                                .filter(route -> route.getRouteState()
                                        .equals(RouteState.NOT_VALIDATED))
                                .toList();
                        break;
                    case "INVALIDATED":
                        allRoutes = allRoutes.stream()
                                .filter(route -> route.getRouteState()
                                        .equals(RouteState.INVALIDATED))
                                .toList();
                        break;
                    default:
                        break;
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return new String();
    }

}
