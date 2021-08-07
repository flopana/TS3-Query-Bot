package Commands;

import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.DatabaseClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchNicknameCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        StringBuilder message = new StringBuilder();
        Pattern pattern = Pattern.compile(e.getMessage().substring(16));
        List<User> users = UserManager.getInstance().getAllUsers();
        List<DatabaseClient> databaseClients = ts3Api.getDatabaseClients();

        List<User> alikeUsersOnline = findAlikeUsers(pattern, users);
        List<DatabaseClient> alikeDatabaseClients = findAlikeClients(pattern, databaseClients);

        // TODO: Message strings can be to long. Needs to be split into multiple messages

        if (!alikeUsersOnline.isEmpty()) {
            message.append("Online clients:").append("\n");
            message.append("-------------------").append("\n\n");
            for (User onlineUser : alikeUsersOnline) {
                message.append("uniqueId: ").append(onlineUser.getUniqueId()).append("\n");
                message.append("Nickname: ").append(onlineUser.getNickname()).append("\n\n");
            }
            ts3Api.sendPrivateMessage(e.getInvokerId(), message.toString());
            message = new StringBuilder();
        }

        if (!alikeDatabaseClients.isEmpty()) {
            message.append("Database clients:").append("\n");
            message.append("-------------------").append("\n\n");
            for (DatabaseClient databaseClient : alikeDatabaseClients) {
                message.append("uniqueId: ").append(databaseClient.getUniqueIdentifier()).append("\n");
                message.append("Nickname: ").append(databaseClient.getNickname()).append("\n");
                message.append("LastConnection: ").append(databaseClient.getLastConnectedDate()).append("\n\n");
            }
            ts3Api.sendPrivateMessage(e.getInvokerId(), message.toString());
        }

        if (message.isEmpty()){
            message.append("Couldn't find any clients with pattern: ").append(pattern);
        }
    }

    private List<DatabaseClient> findAlikeClients(Pattern pattern, List<DatabaseClient> databaseClients){
        List<DatabaseClient> alikeDatabaseClients = new ArrayList<>();

        for (DatabaseClient databaseClient : databaseClients){
            Matcher matcher = pattern.matcher(databaseClient.getNickname());
            if (matcher.matches()){
                alikeDatabaseClients.add(databaseClient);
            }
        }

        return alikeDatabaseClients;
    }

    private List<User> findAlikeUsers(Pattern pattern, List<User> users){
        List<User> alikeUsers = new ArrayList<>();

        for (User user : users){
            Matcher matcher = pattern.matcher(user.getNickname());
            if (matcher.matches()){
                alikeUsers.add(user);
            }
        }

        return alikeUsers;
    }
}
