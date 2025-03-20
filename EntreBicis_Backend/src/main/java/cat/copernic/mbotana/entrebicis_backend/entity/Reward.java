package cat.copernic.mbotana.entrebicis_backend.entity;

import cat.copernic.mbotana.entrebicis_backend.entity.enums.RewardState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name; 

    @Column
    private String description;

    @Column
    private String observation;

    @Lob
    private byte[] image;

    @Column
    private Double valuePoints;

    @Column
    @Enumerated(EnumType.STRING)
    private RewardState rewardState;

    @ManyToOne
    private ExchangePoint exchangePoint;

    @OneToOne(mappedBy = "reward")
    private Reservation reservation;

}
