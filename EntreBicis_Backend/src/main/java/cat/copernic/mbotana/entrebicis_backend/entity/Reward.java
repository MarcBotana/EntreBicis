package cat.copernic.mbotana.entrebicis_backend.entity;

import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
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
import jakarta.validation.constraints.NotBlank;
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

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String name; 

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String description;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String observation;

    @Lob
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private byte[] image;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private Double valuePoints;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardState rewardState;

    @ManyToOne
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private ExchangePoint exchangePoint;

    @OneToOne(mappedBy = "reward")
    private Reservation reservation;

}
