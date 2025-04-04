package cat.copernic.mbotana.entrebicis_backend.controller.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/system")
public class WebSystemParamsController {

    @Autowired
    private SystemParamsLogic webSystemParamsLogic;

    @GetMapping("/list")
    public String listSystemParamsPage(Model model) {
        
        List<SystemParams> allSystemParams = new ArrayList<>();

        try {
            allSystemParams = webSystemParamsLogic.getAllSystemParams();
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }
        
        model.addAttribute("allSystemParams", allSystemParams);

        return "systemParams_list";
    }

    @GetMapping("/detail/{id}")
    public String systemParamsDetailPage(@PathVariable Long id, Model model) {

        SystemParams systemParams = new SystemParams();

        try {
            systemParams = webSystemParamsLogic.getSystemParamsById(id);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        model.addAttribute("systemParams", systemParams);

        return "systemParams_detail";
    }
    
    @GetMapping("/update/{id}")
    public String updateSystemParamsPage(@PathVariable Long id ,Model model, @ModelAttribute("exceptionError") String exceptionError) {

        SystemParams systemParams = new SystemParams();

        //Stop Time from 1 Min to 15 Min
        List<Integer> stopTimeList = IntStream.rangeClosed(1, 15).boxed().toList();

        //Collect Time from 12h to 72h
        List<Integer> collTimeList = IntStream.iterate(12, n -> n +12).limit(6).boxed().toList();

        model.addAttribute("stopTimeList", stopTimeList);
        model.addAttribute("collTimeList", collTimeList);

        try {
            systemParams = webSystemParamsLogic.getSystemParamsById(id);
        } catch (DataAccessException e) {
            model.addAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
        } catch (SQLException e) {
            model.addAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
        }  catch (Exception e) {
            model.addAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
        }

        if (systemParams != null) {
            model.addAttribute("systemParams", systemParams);
        } else {
            return "redirect:/system/details?id=1";
        }

        if (exceptionError != null && !exceptionError.isEmpty()) {
            model.addAttribute("exceptionError", exceptionError);
        }

        return "systemParams_update";
    }

    @PutMapping("/update/new")
    public String updateSystemParams(@Valid @ModelAttribute("systemParams") SystemParams newSystemParams, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {    
                    
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("systemParams", newSystemParams);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.systemParams", result);
                return "redirect:/system/update?id=" + URLEncoder.encode(newSystemParams.getId().toString(), StandardCharsets.UTF_8);

            } else {
                webSystemParamsLogic.updateSystemParams(newSystemParams);
            }        

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.DATA_ACCESS_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.SQL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("exceptionError", ErrorMessage.GENERAL_EXCEPTION + e.getMessage());
            return "redirect:/user/create";
        }

        return "redirect:/system/list";
    }
    
}
