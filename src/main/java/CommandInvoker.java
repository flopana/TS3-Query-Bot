import Commands.*;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    Map<String, ICommand> commandMap;
    TS3Api ts3Api;
    BotConfiguration botConfiguration;
    Logger logger;
    int whoAmI;

    public CommandInvoker(TS3Api ts3Api, BotConfiguration botConfiguration) {
        this.commandMap = new HashMap<>();
        this.ts3Api = ts3Api;
        this.botConfiguration = botConfiguration;
        this.logger = LoggerFactory.getLogger(CommandInvoker.class);
        this.whoAmI = ts3Api.whoAmI().getId();

        //Register Commands
        commandMap.put("!bothelp", new BothelpCommand());
        commandMap.put("!setnickname", new SetNicknameCommand());

        commandMap.put("!searchnickname", new SearchNicknameCommand());

        commandMap.put("!listclients", new ListClientsCommand());
        commandMap.put("!listchannels", new ListChannelsCommand());
        commandMap.put("!listservergroups", new ListServerGroupsCommand());

        commandMap.put("!msgall", new MessageAllCommand());

        commandMap.put("!rebuildUserManager", new RebuildUserManagerCommand());
        commandMap.put("!showwastedtime", new ShowTimeWastedCommand());
        commandMap.put("!resourceusage", new ResourceUsageCommand());
    }

    public void invokeCommand(TextMessageEvent e) {
        //Check whether the TextEvent originated from the Bot itself or not
        if (whoAmI == e.getInvokerId()) {
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
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Invalid command: " + e.getMessage() + "\nSend me !bothelp for a list of all available commands");
        }
    }
}
