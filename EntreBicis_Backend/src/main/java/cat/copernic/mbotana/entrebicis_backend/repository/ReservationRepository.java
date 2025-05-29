package cat.copernic.mbotana.entrebicis_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.mbotana.entrebicis_backend.entity.Reservation;
import cat.copernic.mbotana.entrebicis_backend.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

        List<Reservation> findReservationByUser(User user);


}
