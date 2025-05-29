package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum RouteState {
    VALIDATED("VALIDADA"), 
    NOT_VALIDATED("NO VALIDADA"),
    PENDING("PENDENT");

    private final String display;

    RouteState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
