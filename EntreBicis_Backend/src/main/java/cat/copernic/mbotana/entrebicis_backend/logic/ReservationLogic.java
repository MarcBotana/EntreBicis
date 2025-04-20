package cat.copernic.mbotana.entrebicis_backend.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.mbotana.entrebicis_backend.entity.Reservation;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.repository.ReservationRepository;

@Service
public class ReservationLogic {

    @Autowired
    private ReservationRepository reservationRepository;

    public void saveReservation(Reservation reservation) throws Exception {
        reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) throws Exception {
        return reservationRepository.findById(id).get();
    }

    public List<Reservation> getAllReservation() throws Exception {
        return reservationRepository.findAll();
    }

    public List<Reservation> getAllUserReservations(User user) throws Exception {
        return reservationRepository.findReservationByUser(user);
    }

    public void updateReservation(Reservation reservation) throws Exception {
        reservationRepository.save(reservation);
    }

    public Boolean existReservationById(Long id) throws Exception {
        return reservationRepository.existsById(id); 
    }

}
