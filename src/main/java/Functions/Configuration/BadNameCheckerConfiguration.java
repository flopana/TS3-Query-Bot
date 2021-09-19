package Functions.Configuration;

public class BadNameCheckerConfiguration {
    private final String message;
    private final String pattern;
    private final Boolean caseSensitive;

    public BadNameCheckerConfiguration(String message, String pattern, Boolean caseSensitive) {
        this.message = message;
        this.pattern = pattern;
        this.caseSensitive = caseSensitive;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    public String getPattern() {
        return pattern;
    }
}
