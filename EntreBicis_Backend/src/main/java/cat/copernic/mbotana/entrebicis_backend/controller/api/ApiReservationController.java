package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.ReservationState;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RewardState;
import cat.copernic.mbotana.entrebicis_backend.logic.ReservationLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.RewardLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

@RestController
@RequestMapping("/api/reservation")
public class ApiReservationController {

    @Autowired
    private ReservationLogic apiReservationLogic;

    @Autowired
    private SystemParamsLogic apiSystemParamsLogic;

    @Autowired
    private RewardLogic apiRewardLogic;

    @Autowired 
    private UserLogic apiUserLogic;

    @PostMapping("/create/{email}/{rewardId}")
    public ResponseEntity<List<Reward>> createReservation(@PathVariable String email, @PathVariable Long rewardId) {

        ResponseEntity<List<Reward>> response = null;

        Reservation newReservation = null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiUserLogic.existUserByEmail(email) && !apiRewardLogic.existRewardById(rewardId)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else { 
                SystemParams systemParams = apiSystemParamsLogic.getSystemParamsById(1L);
                int systemCollectionHours = systemParams.getCollectionMaxTime();

                User user = apiUserLogic.getUserByEmail(email);
                Reward reward = apiRewardLogic.getRewardById(rewardId);

                newReservation = new Reservation(
                    null, 
                    ReservationState.ACTIVE, 
                    LocalDateTime.now(), 
                    LocalDateTime.now().plusHours(systemCollectionHours).withHour(23).withMinute(59).withSecond(0).withNano(0), 
                    user, 
                    reward
                );

                apiReservationLogic.saveReservation(newReservation);
                
                reward.setRewardState(RewardState.RESERVED);
                apiRewardLogic.updateReward(reward);

                response = new ResponseEntity<>(headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/list/available")
    public ResponseEntity<List<Reward>> getAvailableRewardList() {

        ResponseEntity<List<Reward>> response = null;

        List<Reward> allRewards = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            allRewards = apiRewardLogic.getAllRewards();
            allRewards.stream()
                    .filter(reward -> reward.getRewardState().equals(RewardState.AVAILABLE)).toList();
                    
            response = new ResponseEntity<>(allRewards, headers, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Reward> getRewardDetail(@PathVariable Long id) {

        ResponseEntity<Reward> response = null;

        Reward reward = new Reward();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiRewardLogic.existRewardById(id)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                reward = apiRewardLogic.getRewardById(id);
                response = new ResponseEntity<>(reward, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
