package cat.copernic.mbotana.entrebicis_backend.entity;

import java.util.List;

import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.UserState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String password;

    @Column
    private String town;

    @Column
    private int mobile;

    @Lob
    private byte[] image;

    @Column
    @Enumerated(EnumType.STRING)
    private UserState userState;

    @Column
    private Double totalPoints;

    @OneToMany(mappedBy = "user")
    private List<Route> routes;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

}
