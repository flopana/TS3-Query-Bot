package Commands;

import Database.DB;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;


public class OnlineTimeCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        User user = UserManager.getInstance().getUser(e.getInvokerId());
        Long[] times = DB.getInstance().getOnlineTimeForUser(user);
        ts3Api.sendPrivateMessage(e.getInvokerId(), String.format("""
            Online time:
            -----------------
            %d seconds (with afk) <-- Only updates when you leave/join the server
            %d seconds (without afk)
            """, times[0]/1000, times[1]));
    }
}
