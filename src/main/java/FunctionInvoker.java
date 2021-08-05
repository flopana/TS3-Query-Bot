import Functions.FunctionInterface;
import Functions.IdleCheck;
import Functions.ServerGroupNotifier;
import com.github.theholywaffle.teamspeak3.TS3Api;

import java.util.HashMap;

public class FunctionInvoker {
    HashMap<String, FunctionInterface> functionMap;
    TS3Api ts3Api;
    BotConfiguration botConfiguration;

    public FunctionInvoker(TS3Api ts3Api, BotConfiguration botConfiguration) {
        this.functionMap = new HashMap<>();
        this.ts3Api = ts3Api;
        this.botConfiguration = botConfiguration;

        functionMap.put("serverGroupNotifier", new ServerGroupNotifier());
        functionMap.put("idleCheck", new IdleCheck());
    }

    public void registerFunctions() {
        for(BotConfiguration.Function function : botConfiguration.getFunctions()){
            try {
                functionMap.get(function.getType()).register(ts3Api, function.getConfigPath());
            } catch (NullPointerException e) {
                System.out.println("Invalid function type: " + function.getType());
            }
        }
    }
}
