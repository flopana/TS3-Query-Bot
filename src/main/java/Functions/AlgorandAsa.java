package Functions;

import Algorand.AlgorandWallet;
import Functions.Configuration.AlgorandConfiguration;
import Interfaces.FunctionInterface;
import com.github.theholywaffle.teamspeak3.TS3Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

public class AlgorandAsa extends ConfigurationReader implements FunctionInterface {
    private static Boolean isInstantiated = false;
    private static AlgorandConfiguration config;

    private static AlgorandWallet algorandWallet;
    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(AlgorandAsa.class);
        final AlgorandConfiguration config = getConfig(AlgorandConfiguration.class, path);
        isInstantiated = true;
        AlgorandAsa.config = config;
        try {
            algorandWallet = new AlgorandWallet(config.getMnemonicSeedOfReserveAccount(),
                                                config.getALGOD_API_ADDR(),
                                                config.getALGOD_API_PORT(),
                                                config.getALGOD_API_TOKEN_KEY(),
                                                config.getALGOD_API_TOKEN());
        } catch (Exception e) {
            logger.error("Error while creating AlgorandWallet: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // TODO: Check here for orders and process them.
    }

    public static Boolean isInstantiated() {
        return isInstantiated;
    }

    public static AlgorandConfiguration getConfig() {
        if (!isInstantiated) {
            throw new RuntimeException("AlgorandAsa is not instantiated!");
        }
        return config;
    }

    public static AlgorandWallet getAlgorandWallet() {
        if (!isInstantiated) {
            throw new RuntimeException("AlgorandAsa is not instantiated!");
        }
        return algorandWallet;
    }
}