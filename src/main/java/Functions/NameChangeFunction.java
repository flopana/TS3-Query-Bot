package Functions;

import Interfaces.FunctionInterface;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NameChangeFunction implements FunctionInterface {
    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(NameChangeFunction.class);

        Thread thread = new Thread(() -> {
            while (true) {
                logger.debug("Checking for name changes.");

                List<Client> clients = ts3Api.getClients();
                UserManager userManager = UserManager.getInstance();

                for(Client client : clients){
                    User user = userManager.getUser(client.getId());

                    // Race condition it can happen that the teamspeak user table contains users that aren't in the user manager
                    if (user == null){
                        continue;
                    }

                    if (!client.getNickname().equals(user.getNickname())){
                        userManager.removeUser(client.getId());
                        userManager.addUser(client, userManager.getAdminGroupIds());
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setDaemon(true);
        logger.info("Starting NameChange function");
        thread.start();
    }
}
