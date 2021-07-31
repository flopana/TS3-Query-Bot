package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import java.util.List;

public class ListChannelsCommand implements Command {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        List<Channel> channels = ts3Api.getChannels();
        StringBuilder stringBuilder = new StringBuilder();

        for (Channel channel : channels) {
            stringBuilder.append("Name: ").append(channel.getName()).append("\n");
            stringBuilder.append("Id: ").append(channel.getId()).append("\n");
            stringBuilder.append("\n");
        }

        ts3Api.sendPrivateMessage(e.getInvokerId(), stringBuilder.toString());
    }
}
