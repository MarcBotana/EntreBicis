package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum UserState {
    ACTIVE("Actiu"),
    INACTIVE("Inactiu");

    private final String display;

    UserState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
