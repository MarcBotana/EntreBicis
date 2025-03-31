package cat.copernic.mbotana.entrebicis_backend.config;

import java.util.NavigableMap;
import java.util.TreeMap;

public class MapPointsConfig {

    public static final NavigableMap<Integer, Double> POINT_REFRESH = new TreeMap<>();

    static {
        POINT_REFRESH.put(1, 0.5);

        for (int speed = 5; speed <= DataFormat.MAX_SYS_VEL; speed += 5) {
            double seconds = 0.5 + (speed / 10.0);
            POINT_REFRESH.put(speed, seconds);
        }
    }

    public static double getUpdateTimeForSpeed(int speed) {
        if (POINT_REFRESH.floorEntry(speed) != null) {
            return POINT_REFRESH.floorEntry(speed).getValue();
        } else{
            return 0.5;
        }
    }
}
