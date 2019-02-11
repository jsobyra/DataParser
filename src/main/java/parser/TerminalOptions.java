package parser;

import org.apache.commons.cli.*;

public class TerminalOptions {
    private Options options;
    private CommandLineParser parser;
    private CommandLine commandLine;


    public TerminalOptions(String[] terminalArguments) {
        options = new Options()
                .addOption(null, "download", false, "Download and generate full maps")
                .addOption(null, "heatmap", false, "Create heat map")
                .addOption(null, "latitude", true, "Latitude of center of down left square")
                .addOption(null, "longitude", true, "Longitude of center of down left square")
                .addOption(null, "maps", true, "Amount of full maps")
                .addOption(null, "width", true, "Amount of part maps in width for full map")
                .addOption(null, "height", true, "Amount of part maps in height for full map")
                .addOption(null, "path", true, "Directory path")
                .addOption(null, "maxacc", true, "Max acceleration")
                .addOption(null, "key", true, "Google api key");
        parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, terminalArguments);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Problems encountered while parsing the command line tokens. Please recheck");
            System.exit(-1);
        }
    }

    public boolean containFlag(String flag) {
        return commandLine.hasOption(flag);
    }

    public String getFlagValue(String flag) {
        return commandLine.getOptionValue(flag);
    }
}
