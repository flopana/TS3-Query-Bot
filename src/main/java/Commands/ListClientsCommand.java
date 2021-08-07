package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.List;

public class ListClientsCommand implements Command {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        List<Client> clients = ts3Api.getClients();
        StringBuilder stringBuilder = new StringBuilder();

        for (Client client : clients) {
            stringBuilder.append("Nickame: ").append(client.getNickname()).append("\n");
            stringBuilder.append("clientId: ").append(client.getId()).append("\n");
            stringBuilder.append("DatabaseId: ").append(client.getDatabaseId()).append("\n");
            stringBuilder.append("UUID: ").append(client.getUniqueIdentifier()).append("\n");
            stringBuilder.append("Ip: ").append(client.getIp()).append("\n");
            stringBuilder.append("channelId: ").append(client.getChannelId()).append("\n");
            stringBuilder.append("\n");
        }

        ts3Api.sendPrivateMessage(e.getInvokerId(), stringBuilder.toString());
    }
}
