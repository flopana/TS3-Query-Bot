package UserManagement;

import Database.Model.UserDBModel;

import java.util.Timer;

public class User {
    private int clientId;
    private String uniqueId;
    private int dbId;
    private String nickname;
    private boolean isAdmin;
    private int[] serverGroupIds;
    private int currentChannelId;
    private String ipAddress;
    private long startTime, endTime;

    private UserDBModel userDBModel;

    public User(int clientId, String uniqueId, int dbId, String nickname, boolean isAdmin, int[] serverGroupIds, int currentChannelId, String ipAddress, UserDBModel userDBModel) {
        this.clientId = clientId;
        this.uniqueId = uniqueId;
        this.dbId = dbId;
        this.nickname = nickname;
        this.isAdmin = isAdmin;
        this.serverGroupIds = serverGroupIds;
        this.currentChannelId = currentChannelId;
        this.ipAddress = ipAddress;
        this.userDBModel = userDBModel;
        this.startTime = System.currentTimeMillis();
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int[] getServerGroupIds() {
        return serverGroupIds;
    }

    public void setServerGroupIds(int[] serverGroupIds) {
        this.serverGroupIds = serverGroupIds;
    }

    public int getCurrentChannelId() {
        return currentChannelId;
    }

    public void setCurrentChannelId(int currentChannelId) {
        this.currentChannelId = currentChannelId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public UserDBModel getUserDBModel() { return userDBModel;}

    public long getTimeStayed() {
        endTime = System.currentTimeMillis();
        long tempTime = startTime;
        startTime = System.currentTimeMillis();
        return (endTime - startTime);
    }
}
