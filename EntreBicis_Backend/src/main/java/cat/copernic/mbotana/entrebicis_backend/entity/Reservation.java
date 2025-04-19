package cat.copernic.mbotana.entrebicis_backend.entity;

import java.time.LocalDateTime;

import cat.copernic.mbotana.entrebicis_backend.entity.enums.ReservationState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private ReservationState reservationState;

    @Column
    private LocalDateTime reservationTime;

    @Column
    private LocalDateTime returnTime;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "reward_id", unique = true)
    private Reward reward;    

}
