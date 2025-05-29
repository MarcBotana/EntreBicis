package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum UserState {
    ACTIVE("ACTIU"),
    INACTIVE("INACTIU");

    private final String display;

    UserState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
