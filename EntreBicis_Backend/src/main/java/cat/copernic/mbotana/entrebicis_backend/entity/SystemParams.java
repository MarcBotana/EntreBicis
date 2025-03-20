package cat.copernic.mbotana.entrebicis_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SystemParams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int maxVelocity;

    @Column(nullable = false)
    private Double pointsConversion;

    @Column(nullable = false)
    private Double stopMaxTime;

    @Column(nullable = false)
    private Double collectionMaxTime;
    
}
