package parser;

import java.nio.file.Paths;

public class App {

    public static void main(String[] args) {

        if(args.length != 2)
            System.out.println("Not correct amount of parameters!!!");
        else {
            PostgresWriter manager = new PostgresWriter();
            System.out.println("Connecting with database");
            String type = args[0];
            String path = args[1];
            if(type.toUpperCase().equals("STRAVA")) {
                System.out.println("Parsing data for: " + type);
                System.out.println("Parsing data from directory: " + path);
                manager.writeLocations(Paths.get(path), StravaLocation.class, ParsingHelper.standardParser);
            } else if(type.toUpperCase().equals("WAVELO")) {
                System.out.println("Parsing data for: " + type);
                System.out.println("Parsing data from directory: " + path);
                manager.writeLocations(Paths.get(path), WaveloLocation.class, ParsingHelper.standardParser);
            } else {
                System.out.println("Not correct type!!! Try Wavelo or Strava type!!!");
            }

        }
    }
}



