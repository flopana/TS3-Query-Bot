package Commands;

import Database.DB;
import Functions.AlgorandAsa;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GetOutstandingASACommand implements ICommand{
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        User user = UserManager.getInstance().getUser(e.getInvokerId());
        BigDecimal outstandingAsa = BigDecimal.valueOf(DB.getInstance().getOutstandingASA(user)).setScale(2, RoundingMode.HALF_EVEN);
        ts3Api.sendPrivateMessage(e.getInvokerId(), "You have " + outstandingAsa + " " + AlgorandAsa.getConfig().getAssetUnitName() +" left for withdrawal.");
    }
}
