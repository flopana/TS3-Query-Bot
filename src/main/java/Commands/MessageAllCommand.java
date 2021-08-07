package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageAllCommand implements ICommand {

    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        Logger logger = LoggerFactory.getLogger(MessageAllCommand.class);
        List<Client> clients = ts3Api.getClients();
        
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(e.getInvokerName()).append(" writes to everyone:\n\n");
        messageBuilder.append(e.getMessage().substring(8)); //Cuts "!msgall " from the beginning

        for (Client client : clients){
            try {
                ts3Api.sendPrivateMessage(client.getId(), messageBuilder.toString());
            } catch (Exception ex) {
                logger.info("Couldn't send message to: '" + client.getNickname() + "' most likely because the bot tried reaching someone it hasnt got any access to.");
            }
        }
        logger.info("Send message: "+messageBuilder.toString());
    }
}
