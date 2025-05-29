package cat.copernic.mbotana.entrebicis_backend.entity.enums;

public enum RewardState {
    AVAILABLE("DISPONIBLE"), 
    RESERVED("RESERVAT"), 
    ASSIGNED("ASSIGNAT"), 
    RETURNED("RETORNAT");

    private final String display;

    RewardState(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
