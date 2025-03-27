package cat.copernic.mbotana.entrebicis_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserValidator userValidator;

    public SecurityConfig(UserValidator userValidator, UserLogic userLogic) {
        this.userValidator = userValidator;
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/logout", "/css/**", "/images/**", "/scripts/**").permitAll()
                        .requestMatchers("/**").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers("/user/**").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers("/reward/**").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers("/exchangePoint/**").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers("/route/**").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers("/system/**").hasAuthority(Role.ADMIN.toString())
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                .userDetailsService(userValidator);

        return http.build();
    }

    @SuppressWarnings("removal")
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(new UserValidator())
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

