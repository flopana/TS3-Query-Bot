import Functions.ServerGroupNotifier;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BotConfiguration {
    private String botName;
    private String queryUsername;
    private String queryPassword;
    private String serverIp;
    private int virtualServerId;
    private int[] adminGroupIds;
    private Function[] functions;

    static class Function {
        String type;
        String configPath;

        public Function(String type, String configPath) {
            this.type = type;
            this.configPath = configPath;
        }

        public String getType() {
            return type;
        }

        public String getConfigPath() {
            return configPath;
        }
    }

    public BotConfiguration(String botName, String queryUsername, String queryPassword, String serverIp, int virtualServerId, int[] adminGroupIds, Function[] functions) {
        this.botName = botName;
        this.queryUsername = queryUsername;
        this.queryPassword = queryPassword;
        this.serverIp = serverIp;
        this.virtualServerId = virtualServerId;
        this.adminGroupIds = adminGroupIds;
        this.functions = functions;
    }

    public static BotConfiguration loadAndGetConfig() {
        Logger logger = LoggerFactory.getLogger(BotConfiguration.class);
        logger.info("Reading config for class: " + BotConfiguration.class);
        Gson gson = new Gson();
        String configJson = "";
        try {
            configJson = Files.readString(Paths.get("configs/config.json"));
        } catch (IOException e) {
            logger.error("No configs found!");
        }

        return gson.fromJson(configJson, BotConfiguration.class);
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getQueryUsername() {
        return queryUsername;
    }

    public void setQueryUsername(String queryUsername) {
        this.queryUsername = queryUsername;
    }

    public String getQueryPassword() {
        return queryPassword;
    }

    public void setQueryPassword(String queryPassword) {
        this.queryPassword = queryPassword;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getVirtualServerId() {
        return virtualServerId;
    }

    public void setVirtualServerId(int virtualServerId) {
        this.virtualServerId = virtualServerId;
    }

    public int[] getAdminGroupIds() {
        return adminGroupIds;
    }

    public void setAdminGroupIds(int[] adminGroupIds) {
        this.adminGroupIds = adminGroupIds;
    }

    public Function[] getFunctions() {
        return functions;
    }

    public void setFunctions(Function[] functions) {
        this.functions = functions;
    }
}
