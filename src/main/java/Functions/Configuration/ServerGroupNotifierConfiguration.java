package Functions.Configuration;

public class ServerGroupNotifierConfiguration {
    private final int serverGroupIdToNotify;
    private final String message;

    public ServerGroupNotifierConfiguration(int serverGroupIdToNotify, String message) {
        this.serverGroupIdToNotify = serverGroupIdToNotify;
        this.message = message;
    }

    public int getServerGroupIdToNotify() {
        return serverGroupIdToNotify;
    }

    public String getMessage() {
        return message;
    }
}
