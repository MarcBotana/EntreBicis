package cat.copernic.mbotana.entrebicis_backend.entity;

import cat.copernic.mbotana.entrebicis_backend.config.DataFormat;
import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = ErrorMessage.NOT_BLANK)
    @Min(value = DataFormat.MIN_SYS_VEL, message = ErrorMessage.SYS_VEL_LENGTH) 
    @Max(value = DataFormat.MAX_SYS_VEL, message = ErrorMessage.SYS_VEL_LENGTH) 
    private int maxVelocity;

    @Column(nullable = false)
    @NotNull(message = ErrorMessage.NOT_BLANK)
    @Min(value = DataFormat.MIN_SYS_POINT, message = ErrorMessage.SYS_POINT_LENGTH) 
    @Max(value = DataFormat.MAX_SYS_POINT, message = ErrorMessage.SYS_POINT_LENGTH) 
    private Double pointsConversion;

    @Column(nullable = false)
    @NotNull(message = ErrorMessage.NOT_BLANK)
    private int stopMaxTime;

    @Column(nullable = false)
    @NotNull(message = ErrorMessage.NOT_BLANK)
    private int collectionMaxTime; 
    
}
