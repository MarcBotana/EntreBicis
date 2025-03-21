package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum Role {
    ADMIN("Administrador"), 
    BIKER("Ciclista");

    private final String display;

    Role(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
