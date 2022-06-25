package Commands;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class BothelpCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        /*
          TODO !lastconnected <dbid>
          TODO !searchDbId <dbid>
         */

        ts3Api.sendPrivateMessage(e.getInvokerId(), """
                Available Commands:

                ### Member Commands ###
                !bothelp - Displays this message
                !registerAlgoWallet <string> - Registers an Algorand wallet for the user
                !onlineTime - Prints the online time of the user
                !getOutstandingASA - Prints the outstanding ASA of the user
                
                ### Admin Commands ###
                !setnickname <string> - Sets the botname
                !searchnickname <regex> - Searches for a client example !searchnickname .*lisa.*
                                
                !listclients - Prints a list of currently connected clients
                !listchannels - Prints a list of channels on this server
                !listservergroups - Prints a list of Servergroups
                
                !msgall <string> - Sends a message to all users.
                
                Misc:
                !rebuildUserManager - Rebuilds the userManager HashMap
                !resourceusage - Prints resources used by the Bot
                !genCryptSafeRandomString <int: length> Generate a cryptographically safe random String
                """);
    }
}
