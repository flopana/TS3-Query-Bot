package Commands;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class BothelpCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        ts3Api.sendPrivateMessage(e.getInvokerId(), """
                Available Commands:

                !bothelp - Displays this message
                !setnickname <string> - Sets the botname
                                
                !searchnickname <string> - Searches for a client
                                
                !listclients - Prints a list of currently connected clients
                !listchannels - Prints a list of channels on this server
                !listservergroups - Prints a list of Servergroups
                
                !msgall <string> - Sends a message to all users.
                """);
    }
}
