package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum Town {
    VILANOVA_I_LA_GELTRÚ("Vilanova i la Geltrú"),
    GARRAF("Garraf");

    private final String display;

    Town(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
