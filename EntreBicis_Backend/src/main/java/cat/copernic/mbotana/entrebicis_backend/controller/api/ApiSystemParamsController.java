package cat.copernic.mbotana.entrebicis_backend.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/system")
public class ApiSystemParamsController {

    @Autowired
    private SystemParamsLogic apiSystemParamsLogic;

    @GetMapping("/list/all")
    public ResponseEntity<List<SystemParams>> getAllSystemParams() {

        ResponseEntity<List<SystemParams>> response = null;

        List<SystemParams> allSystemParams = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            allSystemParams = apiSystemParamsLogic.getAllSystemParams();
            response = new ResponseEntity<>(allSystemParams, headers, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<SystemParams> getSystemParamsById(@PathVariable Long id) {
        
        ResponseEntity<SystemParams> response = null;

        SystemParams systemParams = new SystemParams();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");

        try {
            if (!apiSystemParamsLogic.existSystemParamsById(id)) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                systemParams = apiSystemParamsLogic.getSystemParamsById(id);
                response = new ResponseEntity<>(systemParams, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
    
    

}
