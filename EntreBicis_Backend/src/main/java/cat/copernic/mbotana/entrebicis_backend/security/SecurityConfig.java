package cat.copernic.mbotana.entrebicis_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import cat.copernic.mbotana.entrebicis_backend.entity.SystemParams;
import cat.copernic.mbotana.entrebicis_backend.entity.User;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;
import cat.copernic.mbotana.entrebicis_backend.entity.enums.UserState;
import cat.copernic.mbotana.entrebicis_backend.logic.SystemParamsLogic;
import cat.copernic.mbotana.entrebicis_backend.logic.UserLogic;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final UserValidator userValidator;

    @Autowired
    private final UserLogic userLogic;

    @Autowired
    private final SystemParamsLogic systemParamsLogic;

    public SecurityConfig(UserValidator userValidator, UserLogic userLogic, SystemParamsLogic systemParamsLogic) {
        this.userValidator = userValidator;
        this.userLogic = userLogic;
        this.systemParamsLogic = systemParamsLogic;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                        .requestMatchers("/login", "/logout", "/css/**", "/images/**", "/scripts/**").permitAll()
                        .requestMatchers("/**").hasAuthority(Role.ADMIN.toString())
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true"))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((_, response, _) -> {
                            response.sendRedirect("/login?errorDenied=true");
                        }))
                .userDetailsService(userValidator);

        // Generate Default Admin User
        generateAdminUser();

        // Generate Default System Params
        generateSystemParams();

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

    private void generateAdminUser() {
        try {
            if (!userLogic.existUserByEmail("marc.botana@gmail.com")) {
                User newADmin = new User("marc.botana@gmail.com", Role.ADMIN, "Marc", "Botana",
                        passwordEncoder().encode("1234"), "Terrassa", 620016600, null, true, UserState.ACTIVE,
                        0.0, null, null);
                userLogic.saveUser(newADmin);
                System.out.println("Usuari Admin generat!");
            } else {
                System.out.println("Usuari Admin ja generat!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void generateSystemParams() {
        try {
            if (!systemParamsLogic.existSystemParamsById(1L)) {
                SystemParams newSystemParams = new SystemParams(1L, "Paràmetres per les Rutes",
                        "Paràmetres del comportament de l'aplicació amb els recorreguts amb bicicleta.", 120, 1.0, 5,
                        72);
                systemParamsLogic.saveSystemParams(newSystemParams);
                System.out.println("Paràmetres Rutes generats!");
            } else {
                System.out.println("Paràmetres Rutes ja generats!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
