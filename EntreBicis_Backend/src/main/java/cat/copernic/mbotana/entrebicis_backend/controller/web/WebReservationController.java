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
import cat.copernic.mbotana.entrebicis_backend.entity.Reservation;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.ReservationState;
import cat.copernic.mbotana.entrebicis_backend.logic.ReservationLogic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reservation")
public class WebReservationController {

    @Autowired
    ReservationLogic webReservationLogic;

    @GetMapping("/list")
    public String listReservationPage(@RequestParam(required = false) String sort,
            @RequestParam(required = false) String search, Model model) {

        List<Reservation> allReservations = new ArrayList<>();

        try {

            allReservations = webReservationLogic.getAllReservation();

            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "userName":
                        allReservations.sort(Comparator.comparing(reservation -> reservation.getUser().getName()));
                        break;
                    case "rewardName":
                        allReservations.sort(Comparator.comparing(reservation -> reservation.getReward().getName()));
                        break;
                    case "ACTIVE":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState().equals(ReservationState.ACTIVE))
                                .toList();
                        break;
                    case "COMPLETED":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState().equals(ReservationState.COMPLETED))
                                .toList();
                        break;
                    case "CANCELED":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState().equals(ReservationState.CANCELED))
                                .toList();
                        break;
                    default:
                        break;
                }
            }

            if (search != null && !search.isEmpty()) {
                allReservations = allReservations.stream().filter(
                        reservation -> reservation.getUser().getName().toLowerCase().contains(search.toLowerCase())
                                || reservation.getReward().getName().toLowerCase().contains(search.toLowerCase()))
                        .toList();
            }

        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("allReservations", allReservations);

        return "reservation_list";
    }

    @GetMapping("/detail/{id}")
    public String reservationDetailPage(@PathVariable Long id, Model model) {
        
        Reservation reservation = new Reservation();

        try {
            reservation = webReservationLogic.getReservationById(id);
        }  catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("reservation", reservation);

        return "reservation_detail";
    }
    

}
