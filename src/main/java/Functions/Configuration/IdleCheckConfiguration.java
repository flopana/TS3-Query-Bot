package Functions.Configuration;

public class IdleCheckConfiguration {
    private final long idleTimeoutMillis;
    private final int[] serverGroupsToIgnore;
    private final int[] channelsToIgnore;
    private final int channelToMoveTo;

    public IdleCheckConfiguration(long idleTimeoutMillis, int[] serverGroupsToIgnore, int[] channelsToIgnore, int channelToMoveTo) {
        this.idleTimeoutMillis = idleTimeoutMillis;
        this.serverGroupsToIgnore = serverGroupsToIgnore;
        this.channelsToIgnore = channelsToIgnore;
        this.channelToMoveTo = channelToMoveTo;
    }

    public long getIdleTimeoutMillis() {
        return idleTimeoutMillis;
    }

    public int[] getServerGroupsToIgnore() {
        return serverGroupsToIgnore;
    }

    public int[] getChannelsToIgnore() {
        return channelsToIgnore;
    }

    public int getChannelToMoveTo() {
        return channelToMoveTo;
    }
}
