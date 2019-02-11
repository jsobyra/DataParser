package parser;

import java.nio.file.Paths;

public class App {

    public static void main(String[] args) {
        TerminalOptions options = new TerminalOptions(args);

        String path = options.getFlagValue("path");

        PostgresWriter manager = new PostgresWriter();
        System.out.println("Connecting with database");

        if(options.containFlag("strava")) {
            System.out.println("Parsing data for Strava");
            System.out.println("Parsing data from directory: " + path);
            manager.writeLocations(Paths.get(path), StravaLocation.class, ParsingHelper.standardParser);
        }
        else if(options.containFlag("wavelo")) {
            System.out.println("Parsing data for Wavelo");
            System.out.println("Parsing data from directory: " + path);
            manager.writeLocations(Paths.get(path), WaveloLocation.class, ParsingHelper.standardParser);
        }
    }
}



