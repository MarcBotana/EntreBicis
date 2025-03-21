package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum RouteValidate {
    VALIDATED("Validada"), 
    NOT_VALIDATED("No validada");

    private final String display;

    RouteValidate(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
