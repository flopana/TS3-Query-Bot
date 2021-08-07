package Commands;

import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.util.Arrays;
import java.util.List;

public class ListClientsCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        List<User> users = UserManager.getInstance().getAllUsers();
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : users) {
            stringBuilder.append("Nickame: ").append(user.getNickname()).append("\n");
            stringBuilder.append("clientId: ").append(user.getClientId()).append("\n");
            stringBuilder.append("databaseId: ").append(user.getDbId()).append("\n");
            stringBuilder.append("UUID: ").append(user.getUniqueId()).append("\n");
            stringBuilder.append("ServerGroups: ").append(Arrays.toString(user.getServerGroupIds())).append("\n");
            stringBuilder.append("Ip: ").append(user.getIpAddress()).append("\n");
            stringBuilder.append("channelId: ").append(user.getCurrentChannelId()).append("\n");
            stringBuilder.append("\n");
        }

        ts3Api.sendPrivateMessage(e.getInvokerId(), stringBuilder.toString());
    }
}
