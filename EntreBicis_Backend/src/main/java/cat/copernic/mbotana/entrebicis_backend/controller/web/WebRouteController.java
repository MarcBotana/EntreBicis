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
import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RouteState;
import cat.copernic.mbotana.entrebicis_backend.logic.RouteLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequestMapping("/route")
public class WebRouteController {

    @Autowired
    RouteLogic webRouteLogic;

    @Autowired
    UserLogic webUserLogic;

    @Autowired
    SystemParamsLogic webSystemParamsLogic;

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
                    case "PENDING":
                        allRoutes = allRoutes.stream()
                                .filter(route -> route.getRouteState()
                                        .equals(RouteState.PENDING))
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

    @GetMapping("/detail/{id}")
    public String routeDetailPage(@PathVariable Long id, Model model,
            @ModelAttribute("exceptionError") String exceptionError) {

        Route route = new Route();

        SystemParams systemParams = new SystemParams();

        try {
            route = webRouteLogic.getRouteById(id);
            systemParams = webSystemParamsLogic.getSystemParamsById(1L);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        if (route != null && systemParams != null) {
            model.addAttribute("route", route);
            model.addAttribute("systemParams", systemParams);
        } else {
            return "redirect:/route/list";
        }

        return "route_detail";
    }

    @PutMapping("/validate/{id}/{email}")
    public String validateRoute(@PathVariable Long id, @PathVariable String email, Model model,
            RedirectAttributes redirectAttributes) {

        Route route = new Route();

        User user = new User();

        try {

            if (webUserLogic.existUserByEmail(email) && webRouteLogic.existRouteById(id)) {
                
                route = webRouteLogic.getRouteById(id);
                user = webUserLogic.getUserByEmail(email);

                route.setRouteState(RouteState.VALIDATED);
                user.setTotalPoints(user.getTotalPoints() + route.getTotalRutePoints());

                webRouteLogic.updateRoute(route);

                webUserLogic.updateUser(user);
            } else {
                return "redirect:/reservation/list";
            }

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/reservation/detail/" + id;
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/reservation/detail/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/reservation/detail/" + id;
        }

        return "redirect:/route/detail/" + id;
    }

    @PutMapping("/invalidate/{id}/{email}")
    public String invalidateRoute(@PathVariable Long id, @PathVariable String email, Model model,
            RedirectAttributes redirectAttributes) {

        Route route = new Route();

        User user = new User();

        try {

            if (webUserLogic.existUserByEmail(email) && webRouteLogic.existRouteById(id)) {
                
                route = webRouteLogic.getRouteById(id);
                user = webUserLogic.getUserByEmail(email);

                route.setRouteState(RouteState.NOT_VALIDATED);

                if (user.getTotalPoints() > route.getTotalRutePoints()) {
                    user.setTotalPoints(user.getTotalPoints() - route.getTotalRutePoints());
                    webUserLogic.updateUser(user);
                }

                webRouteLogic.updateRoute(route);

            } else {
                return "redirect:/reservation/list";
            }

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/reservation/detail/" + id;
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/reservation/detail/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/reservation/detail/" + id;
        }

        return "redirect:/route/detail/" + id;
    }

}
