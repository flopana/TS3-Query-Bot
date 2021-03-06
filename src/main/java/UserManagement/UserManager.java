package UserManagement;

import Database.DB;
import Interfaces.IObservable;
import Interfaces.IObserver;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UserManager implements IObservable {
    private static final UserManager userManager = new UserManager();
    /*
     * The integer is the Client.clientId which is subject to change when users leave/join (but gets automatically updated)
     * but in basically every Event you get supplied with the clientId which is why its used here as a key
     */
    private final Map<Integer, User> userMap = new HashMap<>();
    private int[] adminGroupIds;
    private final ArrayList<IObserver> observers = new ArrayList<>();
    private User lastUserJoined;
    private Logger logger;

    public UserManager() {
        logger = LoggerFactory.getLogger(UserManager.class);
    }

    public static UserManager getInstance() {
        return userManager;
    }

    public void addUser(User user) {
        lastUserJoined = user;
        userMap.put(user.getClientId(), user);
        DB.getInstance().addUserIfNotExists(user);
        update();
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
            client.getIp()
        );
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
            client.getIp()
        );
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

    public IObservable attach(IObserver observer){
        observers.add(observer);
        return this;
    }

    public IObservable detach(IObserver observer){
        observers.remove(observer);
        return this;
    }

    // TODO maybe make this method private
    // This method is called when a user joins or leaves the server
    public void update(){
        for(IObserver observer : observers){
            observer.update();
        }
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
        DB.getInstance().updateTimeOnlineSum(userMap.get(clientId));
        logger.debug("User Disconnected");
        userMap.remove(clientId);
        update();
    }

    public int getSize(){
        return userMap.size();
    }

    public int[] getAdminGroupIds() {
        return adminGroupIds;
    }

    public User getLastUserJoined(){
        return lastUserJoined;
    }

    public void setAdminGroupIds(int[] adminGroupIds) {
        this.adminGroupIds = adminGroupIds;
    }
}
