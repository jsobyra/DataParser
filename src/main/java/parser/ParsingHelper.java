package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class ParsingHelper {

    public static BiFunction<Path, Class<? extends Location>, List<Route>> standardParser = (path, classLocation) -> {
        List<Route> routes = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(path));
            Pattern pattern = Pattern.compile("<trkseg>([\\s\\S]*?)</trkseg>", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find())
                routes.add(createStandardRoute(matcher.group(1), classLocation));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return routes;
    };

    private static Route createStandardRoute(String stringRoute, Class<? extends Location> classLocation){
        List<Location> locations = new ArrayList<>();
        Pattern pattern = Pattern.compile("<trkpt([\\s\\S]*?)<\\/trkpt>");
        Matcher matcher = pattern.matcher(stringRoute);

        while (matcher.find())
            locations.add(createStandardLocation(matcher.group(1), classLocation));

        return new Route(locations);
    }

    private static Location createStandardLocation(String stringLocation, Class<? extends Location> classLocation) {
        double latitude = getStandardLatitude(stringLocation);
        double longitude = getStandardLongitude(stringLocation);
        LocalDateTime time = getStandardTime(stringLocation);
        if(WaveloLocation.class.equals(classLocation))
            return new WaveloLocation(latitude, longitude, time.toString());
        else if(StravaLocation.class.equals(classLocation))
            return new StravaLocation(latitude, longitude, time.toString());
        else return null;
    }

    private static LocalDateTime getStandardTime(String time) {
        Pattern pattern = Pattern.compile("<time>(.*?)<\\/time>");
        Matcher matcher = pattern.matcher(time);
        matcher.find();
        return LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ISO_DATE_TIME);
    }

    private static double getStandardLongitude(String longitude) {
        Pattern pattern = Pattern.compile("lon=\"(.*?)\"");
        Matcher matcher = pattern.matcher(longitude);
        matcher.find();
        return Double.parseDouble(String.format("%.5f", Double.valueOf(matcher.group(1))));
    }

    private static double getStandardLatitude(String latitude) {
        Pattern pattern = Pattern.compile("lat=\"(.*?)\"");
        Matcher matcher = pattern.matcher(latitude);
        matcher.find();
        return Double.parseDouble(String.format("%.5f", Double.valueOf(matcher.group(1))));
    }

    public static long calculateTime(Location x, Location y) {
        return LocalDateTime.from(LocalDateTime.parse(x.getTime(), DateTimeFormatter.ISO_DATE_TIME)).until(LocalDateTime.parse(y.getTime(), DateTimeFormatter.ISO_DATE_TIME), ChronoUnit.SECONDS);
    }

    public static double calculateDistance(Location x, Location y) {
        int radius = 6371000;
        double lat1 = x.getLatitude() * PI / 180;
        double lat2 = y.getLatitude() * PI / 180;
        double difLat = (y.getLatitude() - x.getLatitude()) * PI / 180;
        double difLon = (y.getLongitude() - x.getLongitude()) * PI / 180;

        double a = pow(sin(difLat/2), 2) + cos(lat1) * cos(lat2) * pow(sin(difLon/2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1-a));
        return radius * c;
    }

}
