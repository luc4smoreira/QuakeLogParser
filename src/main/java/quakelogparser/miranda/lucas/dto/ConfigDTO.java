package quakelogparser.miranda.lucas.dto;

public class ConfigDTO {
    public static final String DEFAULT_LOG = "qgames.log";
    public static final String PARAM_FILE = "-file";
    public static final String PARAM_WARNINGS = "-warnings";
    public static final String PARAM_HELP = "-help";

    private String pathToFile = DEFAULT_LOG;
    private boolean showValidationWarnings = false;
    private boolean help = false;

    public boolean isShowValidationWarnings() {
        return showValidationWarnings;
    }

    public void setShowValidationWarnings(boolean showValidationWarnings) {
        this.showValidationWarnings = showValidationWarnings;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
