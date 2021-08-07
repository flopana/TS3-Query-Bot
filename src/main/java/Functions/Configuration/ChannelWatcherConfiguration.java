package Functions.Configuration;

public class ChannelWatcherConfiguration {
    private final int channelToWatch;
    private final int[] ignoreServerGroups;
    private final int[] serverGroupsToNotify;
    private final String clientJoinedMessage;
    private final String clientJoinedMessageType;
    private final String serverGroupMessageType;

    public ChannelWatcherConfiguration(int channelToWatch, int[] ignoreServerGroups, int[] serverGroupsToNotify, String clientJoinedMessage, String clientJoinedMessageType, String serverGroupMessageType) {
        this.channelToWatch = channelToWatch;
        this.ignoreServerGroups = ignoreServerGroups;
        this.serverGroupsToNotify = serverGroupsToNotify;
        this.clientJoinedMessage = clientJoinedMessage;
        this.clientJoinedMessageType = clientJoinedMessageType;
        this.serverGroupMessageType = serverGroupMessageType;
    }

    public int getChannelToWatch() {
        return channelToWatch;
    }

    public int[] getIgnoreServerGroups() {
        return ignoreServerGroups;
    }

    public int[] getServerGroupsToNotify() {
        return serverGroupsToNotify;
    }

    public String getClientJoinedMessage() {
        return clientJoinedMessage;
    }

    public String getClientJoinedMessageType() {
        return clientJoinedMessageType;
    }

    public String getServerGroupMessageType() {
        return serverGroupMessageType;
    }
}
