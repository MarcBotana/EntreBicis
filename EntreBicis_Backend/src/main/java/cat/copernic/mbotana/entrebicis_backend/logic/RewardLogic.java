package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.Reward;
import cat.copernic.mbotana.entrebicis_backend.repository.RewardRepository;

@Service
public class RewardLogic {

    @Autowired
    private RewardRepository rewardRepository;

    public void saveReward(Reward reward) throws Exception {
        rewardRepository.save(reward);
    }

    public void deleteReward(Reward reward) throws Exception {
        rewardRepository.delete(reward);
    }

    public Reward getRewardById(Long id) throws Exception {
        return rewardRepository.findById(id).get();
    }

    public List<Reward> getAllRewards() throws Exception {
        return rewardRepository.findAll();
    }

    public void updateReward(Reward reward) throws Exception {
        rewardRepository.save(reward);
    }

    public Boolean existRewardById(Long id) throws Exception {
        return rewardRepository.existsById(id); 
    }

}
