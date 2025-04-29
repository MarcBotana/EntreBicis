package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum ReservationState {
    PENDING("PENDENT"),
    ACTIVE("ACTIVA"), 
    COMPLETED("COMPLETADA"),
    CANCELED("CANCEL·LADA");

    private final String display;

    ReservationState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
