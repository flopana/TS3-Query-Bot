package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

import java.util.List;

public class ListServerGroupsCommand implements Command {

    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        List<ServerGroup> serverGroups = ts3Api.getServerGroups();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Servergroups:").append("\n");
        stringBuilder.append("-----------------").append("\n");

        for (ServerGroup serverGroup : serverGroups) {
            stringBuilder.append("Id: ").append(serverGroup.getId()).append("\n");
            stringBuilder.append("Name: ").append(serverGroup.getName()).append("\n");
            stringBuilder.append("\n");
        }

        ts3Api.sendPrivateMessage(e.getInvokerId(), stringBuilder.toString());
    }
}
