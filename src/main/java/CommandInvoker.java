import Commands.BothelpCommand;
import Commands.Command;
import Commands.ListClientsCommand;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    Map<String, Command> commandMap;
    TS3Api ts3Api;

    public CommandInvoker(TS3Api ts3Api) {
        this.commandMap = new HashMap<>();
        this.ts3Api = ts3Api;

        //Register Commands
        commandMap.put("!bothelp", new BothelpCommand());
        commandMap.put("!listclients", new ListClientsCommand());
    }

    public void invokeCommand(TextMessageEvent e){
        //Check whether the TextEvent originated from the Bot itself or not
        if (ts3Api.whoAmI().getId() == e.getInvokerId()){
            return;
        }

        try {
            this.commandMap.get(e.getMessage().split(" ")[0]).execute(e, this.ts3Api);
        } catch (Exception ex) {
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Invalid command: "+e.getMessage());
        }
    }
}
