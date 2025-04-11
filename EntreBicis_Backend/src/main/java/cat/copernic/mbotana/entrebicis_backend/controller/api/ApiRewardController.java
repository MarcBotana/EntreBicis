package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.Reward;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RewardState;
import cat.copernic.mbotana.entrebicis_backend.logic.RewardLogic;

@RestController
@RequestMapping("/api/reward")
public class ApiRewardController {

    @Autowired
    private RewardLogic apiRewardLogic;

    @GetMapping("/list/all")
    public ResponseEntity<List<Reward>> getAllRewardList() {

        ResponseEntity<List<Reward>> response = null;

        List<Reward> allRewards = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            allRewards = apiRewardLogic.getAllRewards();
            response = new ResponseEntity<>(allRewards, headers, HttpStatus.OK);
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
