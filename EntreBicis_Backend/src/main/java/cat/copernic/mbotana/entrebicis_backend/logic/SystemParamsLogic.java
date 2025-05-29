package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.repository.SystemParamsRepository;

@Service
public class SystemParamsLogic {

    @Autowired
    private SystemParamsRepository systemParamsRepository;

    public void saveSystemParams(SystemParams systemParams) throws Exception {
        systemParamsRepository.save(systemParams);
    }

    public SystemParams getSystemParamsById(Long id) throws Exception {
        return systemParamsRepository.findById(id).get();
    }

    public List<SystemParams> getAllSystemParams() throws Exception {
        return systemParamsRepository.findAll();
    }

    public void updateSystemParams(SystemParams systemParams) throws Exception {
        systemParamsRepository.save(systemParams);
    }

    public Boolean existSystemParamsById(Long id) throws Exception {
        return systemParamsRepository.existsById(id);
    }

}
