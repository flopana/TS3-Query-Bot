package UserManagement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class User {
    private int clientId;
    private String uniqueId;
    private int dbId;
    private String nickname;
    private boolean isAdmin;
    private int[] serverGroupIds;
    private int currentChannelId;
    private String ipAddress;
    private final long joinTime;

    public User(int clientId, String uniqueId, int dbId, String nickname, boolean isAdmin, int[] serverGroupIds, int currentChannelId, String ipAddress) {
        this.clientId = clientId;
        this.uniqueId = uniqueId;
        this.dbId = dbId;
        this.nickname = nickname;
        this.isAdmin = isAdmin;
        this.serverGroupIds = serverGroupIds;
        this.currentChannelId = currentChannelId;
        this.ipAddress = ipAddress;
        this.joinTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "{" +
                "clientId:" + clientId +
                ", uniqueId:'" + uniqueId + '\'' +
                ", dbId:" + dbId +
                ", nickname:'" + nickname + '\'' +
                ", isAdmin:" + isAdmin +
                ", serverGroupIds:" + Arrays.toString(serverGroupIds) +
                ", currentChannelId:" + currentChannelId +
                ", ipAddress:'" + ipAddress + '\'' +
                ", startTime:" + joinTime +
                '}';
    }

    public JsonObject toJsonObject(){
        JsonObject obj = new JsonObject();

        obj.addProperty("clientId", this.getClientId());
        obj.addProperty("uniqueId", this.getUniqueId());
        obj.addProperty("dbId", this.getDbId());
        obj.addProperty("nickname", this.getNickname());
        obj.addProperty("isAdmin", this.isAdmin());
        obj.add("serverGroupIds", new Gson().toJsonTree(this.getServerGroupIds()));
        obj.addProperty("currentChannelId", this.getCurrentChannelId());
        obj.addProperty("ipAddress", this.getIpAddress());

        return obj;
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

    /**
     * Returns the time in ms the user has stayed on the server.
     *
     * @return long
     */
    public long getTimeStayed() {
        return System.currentTimeMillis() - joinTime;
    }
}
