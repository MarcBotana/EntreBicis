package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.Reservation;
import cat.copernic.mbotana.entrebicis_backend.entity.Reward;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.ReservationState;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RewardState;
import cat.copernic.mbotana.entrebicis_backend.logic.ReservationLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.RewardLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

@RestController
@RequestMapping("/api/reservation")
public class ApiReservationController {

    @Autowired
    private ReservationLogic apiReservationLogic;

    @Autowired
    private RewardLogic apiRewardLogic;

    @Autowired
    private UserLogic apiUserLogic;

    @PostMapping("/create/{email}/{rewardId}")
    public ResponseEntity<Void> createReservation(@PathVariable String email, @PathVariable Long rewardId) {

        ResponseEntity<Void> response = null;

        Reservation newReservation = null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email) && !apiRewardLogic.existRewardById(rewardId)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                User user = apiUserLogic.getUserByEmail(email);
                Reward reward = apiRewardLogic.getRewardById(rewardId);

                String reservationCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

                if (!user.getIsReservationActive()) {
                    if (user.getTotalPoints() >= reward.getValuePoints()) {
                        newReservation = new Reservation(
                                null,
                                reservationCode,
                                ReservationState.RESERVED,
                                null,
                                LocalDateTime.now().withSecond(0).withNano(0),
                                null,
                                null,
                                user,
                                reward);

                        apiReservationLogic.saveReservation(newReservation);

                        reward.setRewardState(RewardState.RESERVED);
                        apiRewardLogic.updateReward(reward);

                        user.setIsReservationActive(true);
                        apiUserLogic.updateUser(user);

                        response = new ResponseEntity<>(headers, HttpStatus.OK);
                    } else {
                        response = new ResponseEntity<>(HttpStatus.PAYMENT_REQUIRED);
                    }
                } else {
                    response = new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/list/{email}")
    public ResponseEntity<List<Reservation>> getUserReservationsList(@PathVariable String email) {

        ResponseEntity<List<Reservation>> response = null;

        List<Reservation> allUserReservations = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                User user = apiUserLogic.getUserByEmail(email);

                allUserReservations = apiReservationLogic.getAllUserReservations(user);
                response = new ResponseEntity<>(allUserReservations, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Reservation> getReservationDetail(@PathVariable Long id) {

        ResponseEntity<Reservation> response = null;

        Reservation reservation = new Reservation();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiReservationLogic.existReservationById(id)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                reservation = apiReservationLogic.getReservationById(id);
                response = new ResponseEntity<>(reservation, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
