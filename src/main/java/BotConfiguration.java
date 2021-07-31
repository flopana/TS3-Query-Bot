import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BotConfiguration {
    private final String botName;
    private final String queryUsername;
    private final String queryPassword;
    private final String serverIp;
    private final int virtualServerId;
    private final int[] adminGroupIds;

    public BotConfiguration(String botName, String queryUsername, String queryPassword, String serverIp, int virtualServerId, int[] adminGroupIds) {
        this.botName = botName;
        this.queryUsername = queryUsername;
        this.queryPassword = queryPassword;
        this.serverIp = serverIp;
        this.virtualServerId = virtualServerId;
        this.adminGroupIds = adminGroupIds;
    }

    public static BotConfiguration loadAndGetConfig() {
        Gson gson = new Gson();
        String configJson = "";
        try {
            configJson = Files.readString(Paths.get("config.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(configJson, BotConfiguration.class);
    }

    public String getBotName() {
        return botName;
    }

    public String getQueryUsername() {
        return queryUsername;
    }

    public String getQueryPassword() {
        return queryPassword;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getVirtualServerId() {
        return virtualServerId;
    }

    public int[] getAdminGroupIds() {
        return adminGroupIds;
    }
}
