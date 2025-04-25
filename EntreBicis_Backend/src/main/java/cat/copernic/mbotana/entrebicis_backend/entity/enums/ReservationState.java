package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum ReservationState {
    PENDING("Pendent"),
    ACTIVE("Activa"), 
    COMPLETED("Completada"),
    CANCELED("Cancel·lada");

    private final String display;

    ReservationState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
