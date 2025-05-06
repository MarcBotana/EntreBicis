package cat.copernic.mbotana.entrebicis_backend.controller.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
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
                    case "userEmail":
                        allRoutes.sort(Comparator.comparing(route -> route.getUser().getEmail()));
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

            if (search != null && !search.isEmpty()) {
                allRoutes = allRoutes.stream().filter(
                    route -> route.getUser().getName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
            }

        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("allRoutes", allRoutes);

        return "route_list";
    }

}
