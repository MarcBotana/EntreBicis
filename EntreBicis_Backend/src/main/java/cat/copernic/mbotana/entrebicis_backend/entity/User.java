package cat.copernic.mbotana.entrebicis_backend.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cat.copernic.mbotana.entrebicis_backend.config.DataFormat;
import cat.copernic.mbotana.entrebicis_backend.config.ErrorMessage;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.UserState;
import cat.copernic.mbotana.entrebicis_backend.security.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class User implements UserDetails{

    @Id
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    @Email(message = ErrorMessage.EMAIL_FORMAT)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = ErrorMessage.NOT_BLANK)
    private Role role;

    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    @Size(max = DataFormat.MAX_USR_NAME_LENGTH, message = ErrorMessage.USR_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = ErrorMessage.NOT_BLANK)
    @Size(max = DataFormat.MAX_USR_SURNAME_LENGTH, message = ErrorMessage.USR_SURNAME_LENGTH)
    private String surname;

    @Column(nullable = false)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] userPermission = permission.split(",");

        for (String perm : userPermission) {
            authorities.add(new Permission(Role.valueOf(perm)));
        }
        
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }



}
