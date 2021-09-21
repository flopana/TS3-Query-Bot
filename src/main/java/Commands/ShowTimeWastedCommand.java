package Commands;

import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.util.List;

public class ShowTimeWastedCommand implements ICommand{
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        StringBuilder stringBuilder = new StringBuilder();
        User user = UserManager.getInstance().getUser(e.getTargetClientId());
        long tage = 0, stunden = 0, minuten = 0, sekunden = 0;
        long secondsWasted = UserManager.getInstance().getUser(e.getTargetClientId()).getUserDBModel().GetWastedTime();
        tage = secondsWasted / 86400;
        secondsWasted %= 86400;
        stunden = secondsWasted / 3600;
        secondsWasted %= 3600;
        minuten = secondsWasted / 60;
        secondsWasted %= 60;
        sekunden = secondsWasted;

        stringBuilder.append("Du hast schon " + tage + " Tage, " + stunden + " Stunden, " + minuten + " Minuten und " + sekunden + " Sekunden gewasted.");
        ts3Api.sendPrivateMessage(e.getInvokerId(), stringBuilder.toString());
    }
}
