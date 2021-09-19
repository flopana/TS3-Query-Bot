import Functions.*;
import com.github.theholywaffle.teamspeak3.TS3Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class FunctionInvoker {
    HashMap<String, FunctionInterface> functionMap;
    TS3Api ts3Api;
    BotConfiguration botConfiguration;
    Logger logger;

    public FunctionInvoker(TS3Api ts3Api, BotConfiguration botConfiguration) {
        this.functionMap = new HashMap<>();
        this.ts3Api = ts3Api;
        this.botConfiguration = botConfiguration;
        this.logger = LoggerFactory.getLogger(FunctionInvoker.class);

        functionMap.put("serverGroupNotifier", new ServerGroupNotifier());
        functionMap.put("idleCheck", new IdleCheck());
        functionMap.put("channelWatcher", new ChannelWatcher());
        functionMap.put("badNameChecker", new BadNameChecker());
    }

    public void registerFunctions() {
        // Checks for name changes and updates the UserManager accordingly
        new NameChangeFunction().register(ts3Api, "");

        for(BotConfiguration.Function function : botConfiguration.getFunctions()){
            try {
                logger.info("Registering function " + function.getType());
                functionMap.get(function.getType()).register(ts3Api, function.getConfigPath());
            } catch (NullPointerException e) {
                logger.warn("Invalid function type: " + function.getType());
            }
        }
    }
}
