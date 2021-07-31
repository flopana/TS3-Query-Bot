import Commands.BothelpCommand;
import Commands.Command;
import Commands.ListChannelsCommand;
import Commands.ListClientsCommand;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    Map<String, Command> commandMap;
    TS3Api ts3Api;
    BotConfiguration botConfiguration;

    public CommandInvoker(TS3Api ts3Api, BotConfiguration botConfiguration) {
        this.commandMap = new HashMap<>();
        this.ts3Api = ts3Api;
        this.botConfiguration = botConfiguration;

        //Register Commands
        commandMap.put("!bothelp", new BothelpCommand());
        commandMap.put("!listclients", new ListClientsCommand());
        commandMap.put("!listchannels", new ListChannelsCommand());
    }

    public void invokeCommand(TextMessageEvent e) {
        //Check whether the TextEvent originated from the Bot itself or not
        if (ts3Api.whoAmI().getId() == e.getInvokerId()) {
            return;
        }

        //Check if client is in an Admin group
        boolean isAdmin = false;
        for (Integer serverGroupId : botConfiguration.getAdminGroupIds()) {
            if (ts3Api.getClientByUId(e.getInvokerUniqueId()).isInServerGroup(serverGroupId)) {
                isAdmin = true;
            }
        }
        if (!isAdmin) {
            return;
        }

        try {
            this.commandMap.get(e.getMessage().split(" ")[0]).execute(e, this.ts3Api);
        } catch (Exception ex) {
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Invalid command: " + e.getMessage());
        }
    }
}
