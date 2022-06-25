package Commands;

import Algorand.AlgorandWallet;
import Database.DB;
import Functions.AlgorandAsa;
import Functions.Configuration.AlgorandConfiguration;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterAlgorandWalletCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        if (!AlgorandAsa.isInstantiated()){
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Algorand Asa functionality is not activated on this bot!");
            return;
        }
        User user = UserManager.getInstance().getUser(e.getInvokerId());
        AlgorandConfiguration config = AlgorandAsa.getConfig();
        Logger logger = LoggerFactory.getLogger(RegisterAlgorandWalletCommand.class);
        AlgorandWallet algorandWallet = null;
        try {
            algorandWallet = new AlgorandWallet(config.getMnemonicSeedOfReserveAccount(),
                                                config.getALGOD_API_ADDR(),
                                                config.getALGOD_API_PORT(),
                                                config.getALGOD_API_TOKEN_KEY(),
                                                config.getALGOD_API_TOKEN());
        } catch (Exception ex) {
            System.out.println("Error while creating AlgorandWallet: " + ex.getMessage());
            logger.error("Error while creating Algorand wallet", ex);
        }

        String wallet = e.getMessage().split(" ")[1];

        //If user is in one of the groups, register the wallet
        for (int i = 0; i < user.getServerGroupIds().length; i++) {
            for (int j = 0; j < config.getServerGroupIdsThatCanEarn().length; j++) {
                if (user.getServerGroupIds()[i] == config.getServerGroupIdsThatCanEarn()[j]) {
                    if (algorandWallet.isValidAddressAndIsSubscribedToAsa(wallet, config.getAssetId())) {
                        DB.getInstance().registerAlgorandWallet(user.getDbId(), wallet);
                        ts3Api.sendPrivateMessage(e.getInvokerId(), "Successfully registered Algorand wallet: " + wallet);
                    }else {
                        ts3Api.sendPrivateMessage(e.getInvokerId(), "Failed to register Algorand wallet: " + wallet);
                        ts3Api.sendPrivateMessage(e.getInvokerId(), "Please make sure the wallet is a valid Algorand address and is subscribed to the "+config.getAssetName()+" asset.");
                    }
                    return;
                }
            }
        }
        //If user is not in one of the groups, send message that he is not allowed to register a wallet
        ts3Api.sendPrivateMessage(e.getInvokerId(), "You are not allowed to register an Algorand wallet.");
    }
}
