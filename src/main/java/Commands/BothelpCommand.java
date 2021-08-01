package Commands;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class BothelpCommand implements Command {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        ts3Api.sendPrivateMessage(e.getInvokerId(), """
                Available Commands:

                !bothelp - Displays this message
                                
                !listclients - Prints a list of currently connected clients
                !listchannels - Prints a list of channels on this server
                
                !msgall <string> - Sends a message to all users.
                """);
    }
}
