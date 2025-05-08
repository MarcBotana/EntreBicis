package cat.copernic.mbotana.entrebicis_backend.controller.web;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
import cat.copernic.mbotana.entrebicis_backend.entity.Reward;
import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.ReservationState;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RewardState;
import cat.copernic.mbotana.entrebicis_backend.logic.ReservationLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.RewardLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservation")
public class WebReservationController {

    @Autowired
    ReservationLogic webReservationLogic;

    @Autowired
    SystemParamsLogic webSystemParamsLogic;

    @Autowired
    UserLogic webUserLogic;

    @Autowired
    RewardLogic webRewardLogic;

    @GetMapping("/list")
    public String listReservationPage(@RequestParam(required = false) String sort,
            @RequestParam(required = false) String search, Model model) {

        List<Reservation> allReservations = new ArrayList<>();

        try {

            allReservations = webReservationLogic.getAllReservation();

            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "userEmail":
                        allReservations.sort(Comparator.comparing(reservation -> reservation.getUser().getEmail()));
                        break;
                    case "rewardName":
                        allReservations.sort(Comparator.comparing(reservation -> reservation.getReward().getName()));
                        break;
                    case "RESERVED":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState()
                                        .equals(ReservationState.RESERVED))
                                .toList();
                        break;
                    case "ASSIGNED":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState()
                                        .equals(ReservationState.ASSIGNED))
                                .toList();
                        break;
                    case "RETURNED":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState()
                                        .equals(ReservationState.RETURNED))
                                .toList();
                        break;
                    case "CANCELED":
                        allReservations = allReservations.stream()
                                .filter(reservation -> reservation.getReservationState()
                                        .equals(ReservationState.CANCELED))
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
    public String reservationDetailPage(@PathVariable Long id, Model model, @ModelAttribute("exceptionError") String exceptionError) {

        Reservation reservation = new Reservation();

        try {
            if (webReservationLogic.existReservationById(id)) {
                reservation = webReservationLogic.getReservationById(id);
                model.addAttribute("reservation", reservation);
            } else {
                return "redirect:/reservation/list";
            }
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

        return "reservation_detail";
    }

    @PutMapping("/assign/{id}/{email}")
    public String reservationAssign(@PathVariable Long id, @PathVariable String email, Model model,
            RedirectAttributes redirectAttributes) {

        Reservation reservation = new Reservation();

        Reward reward = new Reward();

        try {

            if (webUserLogic.existUserByEmail(email) && webReservationLogic.existReservationById(id)) {
                reservation = webReservationLogic.getReservationById(id);
                reward = reservation.getReward();

                SystemParams systemParams = webSystemParamsLogic.getSystemParamsById(1L);

                int systemCollectionHours = systemParams.getCollectionMaxTime();

                reservation.setReservationState(ReservationState.ASSIGNED);    
                reservation.setReturnTime(LocalDateTime.now().plusHours(systemCollectionHours).withHour(23).withMinute(59).withSecond(0).withNano(0));
                reservation.setAssignationDate(LocalDateTime.now().withSecond(0).withNano(0));

                reward.setRewardState(RewardState.ASSIGNED);      
                
                webReservationLogic.updateReservation(reservation);
                webRewardLogic.updateReward(reward);
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

        return "redirect:/reservation/detail/" + id;
    }
}
