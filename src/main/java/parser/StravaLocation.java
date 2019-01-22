package parser;

import java.time.LocalDateTime;

public class StravaLocation extends Location {
    public StravaLocation(double latitude, double longitude, LocalDateTime time) {
        super(latitude, longitude, time);
    }
}
