package cat.copernic.mbotana.entrebicis_backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_code", nullable = false)
    private String tokenCode;

    @Column(name = "exp_date", nullable = false)
    private LocalDateTime expireDate;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User user;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireDate);
    }

    public Token(String tokenCode, User user) {
        this.tokenCode = tokenCode;
        this.user = user;
        this.expireDate = LocalDateTime.now().plusMinutes(15);
    }
}
