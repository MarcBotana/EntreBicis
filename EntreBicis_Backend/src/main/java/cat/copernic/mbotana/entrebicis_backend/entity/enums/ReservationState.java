package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum ReservationState {
    RESERVED("RESERVAT"), 
    ASSIGNED("ASSIGNAT"), 
    RETURNED("RETORNAT"),
    CANCELED("CANCEL·LADA");

    private final String display;

    ReservationState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
