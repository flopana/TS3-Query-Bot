package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.List;

public class MessageAllCommand implements Command {

    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        List<Client> clients = ts3Api.getClients();
        
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(e.getInvokerName()).append(" writes to everyone:\n\n");
        messageBuilder.append(e.getMessage().substring(8)); //Cuts "!msgall " from the beginning

        for (Client client : clients){
            ts3Api.sendPrivateMessage(client.getId(), messageBuilder.toString());
        }
    }
}
