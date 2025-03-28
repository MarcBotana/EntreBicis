package cat.copernic.mbotana.entrebicis_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Long id;

    @Column(nullable = false)
    private String paramsTitle;

    @Column(nullable = false)
    private String paramsDesc;

    @Column(nullable = false)
    private int maxVelocity;

    @Column(nullable = false)
    private Double pointsConversion;

    @Column(nullable = false)
    private int stopMaxTime;

    @Column(nullable = false)
    private int collectionMaxTime; 
    
}
