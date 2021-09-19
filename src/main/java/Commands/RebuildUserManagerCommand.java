package Commands;

import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.List;

public class RebuildUserManagerCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        UserManager userManager = UserManager.getInstance();

        // Clears the usermanager
        userManager.clearUserManager();

        // Inserts every current client
        List<Client> clients = ts3Api.getClients();
        for (Client client : clients){
            userManager.addUser(client, userManager.getAdminGroupIds());
        }

        ts3Api.sendPrivateMessage(e.getInvokerId(), "Rebuild userManager HashMap it contains: " + userManager.getSize() + " users.");
    }
}
