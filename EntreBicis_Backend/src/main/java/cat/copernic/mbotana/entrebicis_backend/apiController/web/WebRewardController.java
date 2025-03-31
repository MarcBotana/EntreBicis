package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.Reward;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RewardState;
import cat.copernic.mbotana.entrebicis_backend.logic.ExchangePointLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.RewardLogic;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/reward")
public class WebRewardController {

    @Autowired
    RewardLogic webRewardLogic;

    @Autowired
    ExchangePointLogic webExchangePointLogic;

    @GetMapping("/create")
    public String createRewardPage(Model model, @ModelAttribute("exceptionError") String exceptionError) {
       
        model.addAttribute("rewardState", RewardState.values());

        try {
            model.addAttribute("exchangePoints", webExchangePointLogic.getAllExchangePoints());
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        if (!model.containsAttribute("reward")) {
            Reward reward = new Reward();
            model.addAttribute("reward", reward);
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "reward_create";
    }

    @PostMapping("/create/new")
    public String createReward(@Valid @ModelAttribute("reward") Reward newReward, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {             
                        
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("reward", newReward);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reward", result);
                return "redirect:/reward/create";

            } else {
                //ExchangePoint exchangePoint = webExchangePointLogic.getExchangePointById(exchangePointId);
                //newReward.setExchangePoint(exchangePoint);
                webRewardLogic.saveReward(newReward);
            }        

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/reward/create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/reward/create";
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/reward/create";
        }

        return "redirect:/reward/list";
    }

    @GetMapping("/list")
    public String listRewardsPage(@RequestParam(required = false) String sort, @RequestParam(required = false) String search, Model model) {

        List<Reward> allRewards = new ArrayList<>();

        try {
            allRewards = webRewardLogic.getAllRewards();

            switch (sort) {
                case "name":
                    allRewards.sort(Comparator.comparing(Reward::getName));
                    break;
                case "exchangePoint":
                    allRewards.sort(Comparator.comparing(reward -> reward.getExchangePoint().getName()));
                    break;
                case "AVAILABLE":
                    allRewards = allRewards.stream().filter(reward -> reward.getRewardState().equals(RewardState.AVAILABLE)).toList();
                    break;
                case "RESERVED":
                    allRewards = allRewards.stream().filter(reward -> reward.getRewardState().equals(RewardState.RESERVED)).toList();
                    break;
                case "ASSIGNED":
                    allRewards = allRewards.stream().filter(reward -> reward.getRewardState().equals(RewardState.ASSIGNED)).toList();
                    break;
                case "RETURNED":
                    allRewards = allRewards.stream().filter(reward -> reward.getRewardState().equals(RewardState.RETURNED)).toList();
                    break;
                default:
                    break;
            }

            if (search != null && !search.isEmpty()) {
                allRewards = allRewards.stream().filter(reward -> reward.getName().toLowerCase().contains(search.toLowerCase())).toList();
            }


        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("allRewards", allRewards);

        return "rewards_list";
    }

    @GetMapping("/detail")
    public String rewardDetailPage(@RequestParam Long id, Model model) {

        Reward reward = new Reward();

        try {
            reward = webRewardLogic.getRewardById(id);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("reward", reward);

        return "reward_detail";
    }

    @GetMapping("/update")
    public String updateRewardPage(@RequestParam(required = true) Long id ,Model model, @ModelAttribute("exceptionError") String exceptionError) {

        model.addAttribute("rewardState", RewardState.values());

        Reward reward = new Reward();

        try {
            reward = webRewardLogic.getRewardById(id);
            model.addAttribute("exchangePoints", webExchangePointLogic.getAllExchangePoints());
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        if (reward != null) {
            model.addAttribute("reward", reward);
        } else {
            return "redirect:/reward/list";
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "reward_update";
    }

    @PostMapping("/update/new")
    public String updateUser(@Valid @ModelAttribute("reward") Reward newReward, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {    
                    
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("reward", newReward);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reward", result);
                return "redirect:/reward/update?id=" + URLEncoder.encode(newReward.getId().toString(), StandardCharsets.UTF_8);

            } else {
                webRewardLogic.updateReward(newReward);
            }        

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/reward/update";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/reward/update";
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/reward/update";
        }

        return "redirect:/reward/list";
    }

}
