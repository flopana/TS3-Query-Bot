package Functions;

import Functions.Configuration.BadNameCheckerConfiguration;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BadNameChecker extends ConfigurationReader implements FunctionInterface, IObserver {
    private String message;
    private Pattern pattern;
    private TS3Api ts3Api;

    public BadNameChecker() {
        UserManager.getInstance().attach(this);
    }

    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(BadNameChecker.class);
        final BadNameCheckerConfiguration conf = getConfig(BadNameCheckerConfiguration.class, path);
        Pattern pattern;
        if (conf.getCaseSensitive()){
            pattern = Pattern.compile(conf.getPattern());
        }else {
            pattern = Pattern.compile(conf.getPattern(), Pattern.CASE_INSENSITIVE);
        }

        this.message = conf.getMessage();
        this.pattern = pattern;
        this.ts3Api = ts3Api;

        // Check every 2000ms every client (name change)
        Thread nameChecker = new Thread(() -> {
            while (true){
                logger.debug("Starting a periodic bad name check");
                for(User user : UserManager.getInstance().getAllUsers()){
                    if (checkForBadName(user.getClientId(), pattern)){
                        logger.info("Kicking client: "+user.getNickname()+". This name was determined bad by regex: "+conf.getPattern());
                        kickClientFromServer(ts3Api, user.getClientId(), conf.getMessage());
                    }
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        nameChecker.setDaemon(true);
        logger.info("Starting periodic bad name check");
        nameChecker.start();
    }

    private boolean checkForBadName(int clientId, Pattern pattern) {
        Matcher matcher;

        // It's possible that a user is null because either it wasn't added or removed in time in the usermanager.
        try {
            User user = UserManager.getInstance().getUser(clientId);
            matcher = pattern.matcher(user.getNickname());
        } catch (NullPointerException ignored) {
            return false;
        }

        return matcher.matches();
    }

    private void kickClientFromServer(TS3Api ts3Api, int clientId, String message){
        try {
            ts3Api.kickClientFromServer(message, clientId);
            UserManager.getInstance().removeUser(clientId);
        } catch (Exception ignore) {}
    }

    public void update(){
        Logger logger = LoggerFactory.getLogger(BadNameChecker.class);
        UserManager userManager = UserManager.getInstance();
        User user = userManager.getLastUserJoined();

        if (checkForBadName(user.getClientId(), pattern)) {
            logger.info("Kicking client: "+user.getNickname()+". This name was determined bad by regex: "+this.pattern.toString());
            kickClientFromServer(ts3Api, user.getClientId(), this.message);
        }
    }
}
