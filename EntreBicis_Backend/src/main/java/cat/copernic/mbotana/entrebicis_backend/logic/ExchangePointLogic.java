package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.ExchangePoint;
import cat.copernic.mbotana.entrebicis_backend.repository.ExchangePointRepository;

@Service
public class ExchangePointLogic {

    @Autowired
    private ExchangePointRepository exchangePointRepository;

    public void saveExchangePoint(ExchangePoint exchangePoint) throws Exception {
        exchangePointRepository.save(exchangePoint);
    }

    public ExchangePoint getExchangePointById(Long id) throws Exception {
        return exchangePointRepository.findById(id).get();
    }

    public List<ExchangePoint> getAllExchangePoints() throws Exception {
        return exchangePointRepository.findAll();
    }

    public void updateExchangePoint(ExchangePoint exchangePoint) throws Exception {
        exchangePointRepository.save(exchangePoint);
    }

    public Boolean existExchangePointById(Long id) throws Exception {
        return exchangePointRepository.existsById(id); 
    }

}
