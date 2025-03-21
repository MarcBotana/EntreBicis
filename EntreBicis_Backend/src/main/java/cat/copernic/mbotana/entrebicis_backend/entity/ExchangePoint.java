package cat.copernic.mbotana.entrebicis_backend.entity;

import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class ExchangePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String address;

}
