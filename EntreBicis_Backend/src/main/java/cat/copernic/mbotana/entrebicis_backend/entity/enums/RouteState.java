package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum RouteState {
    PENDING("Pendent"), 
    IN_PROGRESS("En curs"), 
    FINISHED("Finalitzada"), 
    CANCELED("Cancel·lada");

    private final String display;

    RouteState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
