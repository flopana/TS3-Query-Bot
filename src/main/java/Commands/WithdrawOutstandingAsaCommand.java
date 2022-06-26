package Commands;

import Database.DB;
import Functions.AlgorandAsa;
import Functions.Configuration.AlgorandConfiguration;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WithdrawOutstandingAsaCommand implements ICommand{
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        Logger logger = LoggerFactory.getLogger(WithdrawOutstandingAsaCommand.class);
        User user = UserManager.getInstance().getUser(e.getInvokerId());
        DB db = DB.getInstance();
        String wallet = db.getAlgoWalletAddress(user);
        AlgorandConfiguration algorandConfiguration = AlgorandAsa.getConfig();
        BigDecimal outstandingAsa = BigDecimal.valueOf(DB.getInstance().getOutstandingASA(user)).setScale(algorandConfiguration.getAssetDecimalPlaces(), RoundingMode.HALF_EVEN);
        if (wallet == null) {
            ts3Api.sendPrivateMessage(e.getInvokerId(), "You have no Algorand wallet registered. !bothelp");
            return;
        }
        // Multiply by 10**decimalPlaces of ASA to get the correct amount of ASA to send.
        // To achieve this we can use the unscaledValue function
        long amount = outstandingAsa.unscaledValue().longValueExact();
        if (Double.parseDouble(outstandingAsa.toString()) < algorandConfiguration.getAmountToEarnPerHour()){
            ts3Api.sendPrivateMessage(e.getInvokerId(), "You have not earned enough " + algorandConfiguration.getAssetUnitName() + " yet. You need to earn at least " + algorandConfiguration.getAmountToEarnPerHour() + " for a withdrawal.");
            return;
        }
        try {
            var resp = AlgorandAsa.getAlgorandWallet().sendAsaAsset(wallet, amount, algorandConfiguration.getAssetId());
            db.addAmountToAlreadyWithdrawnAsa(user, amount);
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Withdrew " + outstandingAsa + " " +algorandConfiguration.getAssetUnitName()+ " and send it to your Algorand wallet.");
            ts3Api.sendPrivateMessage(e.getInvokerId(), "View the transaction here: https://algoexplorer.io/tx/"+resp.body().txId);
        } catch (Exception ex) {
            logger.error("Error while sending ASA: " + ex.getMessage());
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Error while sending ASA: please contact the admin");
            throw new RuntimeException(ex);
        }
    }
}
