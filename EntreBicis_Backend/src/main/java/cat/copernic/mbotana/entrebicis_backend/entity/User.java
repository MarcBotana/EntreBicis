package cat.copernic.mbotana.entrebicis_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cat.copernic.mbotana.entrebicis_backend.config.DataFormat;
import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.UserState;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User{

    @Id
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    @Email(message = ErrorMessage.EMAIL_FORMAT)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = ErrorMessage.NOT_BLANK)
    private Role role;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    @Size(max = DataFormat.MAX_USR_NAME_LENGTH, message = ErrorMessage.USR_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    @Size(max = DataFormat.MAX_USR_SURNAME_LENGTH, message = ErrorMessage.USR_SURNAME_LENGTH)
    private String surname;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String password;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    private String town;

    @Column(nullable = false)
    @NotNull(message = ErrorMessage.NOT_BLANK)
    @Min(value = 100000000, message = ErrorMessage.USR_MOBILE_LENGTH) 
    @Max(value = 999999999, message = ErrorMessage.USR_MOBILE_LENGTH) 
    private int mobile;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGTEXT")
    @Basic(fetch = FetchType.LAZY)
    private String image;
    
    @Column(nullable = false)
    private Boolean isPasswordChanged;

    @Column(nullable = false)
    private Boolean isReservationActive;

    @Column
    @Enumerated(EnumType.STRING)
    private UserState userState;

    @Column
    private Double totalPoints;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Route> routes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "reward"})
    private List<Reservation> reservations;

}
