package Functions;

import Functions.Configuration.AlgorandConfiguration;
import Interfaces.FunctionInterface;
import com.github.theholywaffle.teamspeak3.TS3Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandAsa extends ConfigurationReader implements FunctionInterface {
    private static Boolean isInstantiated = false;
    private static AlgorandConfiguration config;
    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(AlgorandAsa.class);
        final AlgorandConfiguration config = getConfig(AlgorandConfiguration.class, path);
        AlgorandAsa.config = config;
        isInstantiated = true;

        // TODO: Check here for orders and process them.
    }

    public static Boolean isInstantiated() {
        return isInstantiated;
    }

    public static AlgorandConfiguration getConfig() {
        return config;
    }
}