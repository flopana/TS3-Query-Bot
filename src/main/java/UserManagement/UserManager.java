package UserManagement;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final UserManager userManager = new UserManager();
    /*
     * The integer is the Client.clientId which is subject to change when users leave/join
     * but in basically every Event you get supplied with the clientId which is why its used here as a key
     */
    private Map<Integer, User> userMap = new HashMap<>();

    public UserManager() {
    }

    public static UserManager getInstance() {
        return userManager;
    }

    public void addUser(User user) {
        userMap.put(user.getClientId(), user);
    }

    public void addUser(ClientJoinEvent e, Client client, int[] adminGroupIds) {
        boolean isAdmin = isInOnOfTheGroups(client, adminGroupIds);
        User user = new User(
                e.getClientId(),
                client.getUniqueIdentifier(),
                e.getClientDatabaseId(),
                e.getClientNickname(),
                isAdmin,
                client.getServerGroups(),
                client.getChannelId(),
                client.getIp());
        addUser(user);
    }

    public void addUser(Client client, int[] adminGroupIds) {
        boolean isAdmin = isInOnOfTheGroups(client, adminGroupIds);
        User user = new User(
                client.getId(),
                client.getUniqueIdentifier(),
                client.getDatabaseId(),
                client.getNickname(),
                true,
                client.getServerGroups(),
                client.getChannelId(),
                client.getIp());
        addUser(user);
    }

    private boolean isInOnOfTheGroups(Client client, int[] serverGroupIds) {
        for (int serverGroupId : serverGroupIds) {
            if (client.isInServerGroup(serverGroupId)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(int clientId) {
        return userMap.get(clientId);
    }

    public void removeUserFromLeaveEvent(int clientId) {
        userMap.remove(clientId);
    }
}
