package cat.copernic.mbotana.entrebicis_backend.entity;

import java.time.LocalTime;
import java.util.List;

import cat.copernic.mbotana.entrebicis_backend.entity.enums.RouteState;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.RouteValidate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RouteState routeState;

    @Column
    private RouteValidate isValidate;

    @Column
    private Double totalRutePoints;

    @Column
    private Double totalRouteDistance;

    @Column 
    private LocalTime totalRouteTime;

    @Column
    private Double maxRouteVelocity;

    @Column 
    private Double avgRouteVelocity;

    @OneToMany(mappedBy = "route")
    private List<GpsPoint> gpsPoints;

    @ManyToOne
    @JoinColumn(name = "user_email", unique = true)
    private User user;

}
