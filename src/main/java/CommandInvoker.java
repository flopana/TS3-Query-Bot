import Commands.*;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    Map<String, Command> commandMap;
    TS3Api ts3Api;
    BotConfiguration botConfiguration;
    Logger logger;

    public CommandInvoker(TS3Api ts3Api, BotConfiguration botConfiguration) {
        this.commandMap = new HashMap<>();
        this.ts3Api = ts3Api;
        this.botConfiguration = botConfiguration;
        this.logger = LoggerFactory.getLogger(CommandInvoker.class);

        //Register Commands
        commandMap.put("!bothelp", new BothelpCommand());
        commandMap.put("!listclients", new ListClientsCommand());
        commandMap.put("!listchannels", new ListChannelsCommand());
        commandMap.put("!listservergroups", new ListServerGroupsCommand());
        commandMap.put("!msgall", new MessageAllCommand());
    }

    public void invokeCommand(TextMessageEvent e) {
        //Check whether the TextEvent originated from the Bot itself or not
        if (ts3Api.whoAmI().getId() == e.getInvokerId()) {
            return;
        }

        logger.info(e.getInvokerName() + " invoked command: " + e.getMessage());

        //Check if client is in an Admin group
        boolean isAdmin = UserManager.getInstance().getUser(e.getInvokerId()).isAdmin();

        if (!isAdmin) {
            logger.info(e.getInvokerName() + " tried invoking a command but is not an Admin");
            ts3Api.sendPrivateMessage(e.getInvokerId(), """
                    You're not an Admin!
                    Valid Admin groups are the following:
                                        
                    """ + Arrays.toString(botConfiguration.getAdminGroupIds()));
            return;
        }

        try {
            this.commandMap.get(e.getMessage().split(" ")[0]).execute(e, this.ts3Api);
        } catch (NullPointerException ex) {
            logger.info("Invalid command by " + e.getInvokerName() + " : " + e.getMessage());
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Invalid command: " + e.getMessage());
        }
    }
}
