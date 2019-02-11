package parser;

import org.apache.commons.cli.*;

public class TerminalOptions {
    private Options options;
    private CommandLineParser parser;
    private CommandLine commandLine;

    public TerminalOptions(String[] terminalArguments) {
        options = new Options()
                .addOption(null, "strava", false, "Read strava mapas")
                .addOption(null, "wavelo", false, "Read wavelo maps")
                .addOption(null, "path", true, "Directory path");
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
