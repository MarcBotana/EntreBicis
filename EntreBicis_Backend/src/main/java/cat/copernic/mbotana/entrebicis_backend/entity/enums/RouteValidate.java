package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum RouteValidate {
    VALIDATED("VALIDADA"), 
    NOT_VALIDATED("NO VALIDADA");

    private final String display;

    RouteValidate(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
