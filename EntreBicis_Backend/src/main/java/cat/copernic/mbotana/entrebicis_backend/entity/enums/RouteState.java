package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum RouteState {
    VALIDATED("VALIDADA"), 
    NOT_VALIDATED("NO VALIDADA"),
    NOT_VERIFIED("NO VERIFICADA");

    private final String display;

    RouteState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
