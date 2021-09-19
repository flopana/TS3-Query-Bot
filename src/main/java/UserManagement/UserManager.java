package UserManagement;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.*;

public class UserManager {
    private static final UserManager userManager = new UserManager();
    /*
     * The integer is the Client.clientId which is subject to change when users leave/join (but gets automatically updated)
     * but in basically every Event you get supplied with the clientId which is why its used here as a key
     */
    private final Map<Integer, User> userMap = new HashMap<>();
    private int[] adminGroupIds;

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
                isAdmin,
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

    public List<User> getAllUsers(){
        Set<Integer> keys = userMap.keySet();
        List<User> users = new ArrayList<>();

        for (Integer key : keys){
            users.add(userMap.get(key));
        }

        return users;
    }

    public void clearUserManager(){
        userMap.clear();
    }

    public void removeUser(int clientId) {
        userMap.remove(clientId);
    }

    public int getSize(){
        return userMap.size();
    }

    public int[] getAdminGroupIds() {
        return adminGroupIds;
    }

    public void setAdminGroupIds(int[] adminGroupIds) {
        this.adminGroupIds = adminGroupIds;
    }
}
