package cat.copernic.mbotana.entrebicis_backend.apiController.web;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.ExchangePoint;
import cat.copernic.mbotana.entrebicis_backend.logic.ExchangePointLogic;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/exchangePoint")
public class WebExchangePointController {

    @Autowired
    ExchangePointLogic webExchangePointLogic;

    @GetMapping("/create")
    public String createExchangePointPage(Model model, @ModelAttribute("exceptionError") String exceptionError) {
       
        try {
            model.addAttribute("exchangePoints", webExchangePointLogic.getAllExchangePoints());
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        if (!model.containsAttribute("exchangePoint")) {
            ExchangePoint exchangePoint = new ExchangePoint();
            model.addAttribute("exchangePoint", exchangePoint);
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "exchangePoint_create";
    }

    @PostMapping("/create/new")
    public String createExchangePoint(@Valid @ModelAttribute("exchangePoint") ExchangePoint newExchangePoint, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {              
            
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("exchangePoint", newExchangePoint);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exchangePoint", result);
                return "redirect:/exchangePoint/create";

            } else {
                webExchangePointLogic.saveExchangePoint(newExchangePoint);
            }        

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/exchangePoint/create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/exchangePoint/create";
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/exchangePoint/create";
        }

        return "redirect:/reward/list";
    }
}
