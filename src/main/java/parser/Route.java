package parser;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private final List<Location> locations;

    public Route(List<Location> locations) {
        locations = filterLocations(locations);
        locations = calculateVelocities(locations);
        locations = calculateAccelerations(locations);
        this.locations = locations;
    }

    private List<Location> filterLocations(List<Location> locations) {
        List<Location> doubledLocations = new ArrayList<>();
        for(int i = 1; i < locations.size(); i++) {
            if(locations.get(i-1).equals(locations.get(i)))
                doubledLocations.add(locations.get(i-1));
        }
        locations.removeAll(doubledLocations);
        return locations;
    }

    private List<Location> calculateVelocities(List<Location> locations) {
        for(int i = 1; i < locations.size(); i++) {
            double distance = ParsingHelper.calculateDistance(locations.get(i-1), locations.get(i));
            long time = ParsingHelper.calculateTime(locations.get(i-1), locations.get(i));
            locations.get(i).setVelocity(distance/time);
        }
        return locations;
    }

    private List<Location> calculateAccelerations(List<Location> locations) {
        for(int i = 1; i < locations.size(); i++) {
            double diffVelocity = locations.get(i).getVelocity() - locations.get(i-1).getVelocity();
            long time = ParsingHelper.calculateTime(locations.get(i-1), locations.get(i));
            locations.get(i-1).setAcceleration(diffVelocity/time);
        }
        return locations;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
