package cat.copernic.mbotana.entrebicis_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.mbotana.entrebicis_backend.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
