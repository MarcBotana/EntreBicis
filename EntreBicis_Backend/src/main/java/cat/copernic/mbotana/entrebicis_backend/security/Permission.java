package cat.copernic.mbotana.entrebicis_backend.security;

import org.springframework.security.core.GrantedAuthority;

import cat.copernic.mbotana.entrebicis_backend.entity.enums.Role;

public class Permission implements GrantedAuthority {

    private Role role;

    public Permission(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.toString();
    }

}
