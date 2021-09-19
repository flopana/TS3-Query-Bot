package Functions;

import Functions.Configuration.BadNameCheckerConfiguration;
import UserManagement.User;
import UserManagement.UserManager;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BadNameChecker extends ConfigurationReader implements FunctionInterface {
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

        // Check directly when a client joins
        ts3Api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent e) {
                if (checkForBadName(e.getClientId(), pattern)) {
                    logger.info("Kicking client: "+e.getClientNickname()+". This name was determined bad by regex: "+conf.getPattern());
                    kickClientFromServer(ts3Api, e.getClientId(), conf.getMessage());
                }
            }
        });

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
            UserManager userManager = UserManager.getInstance();
            ts3Api.kickClientFromServer(message, clientId);
        } catch (Exception ignore) {}
    }
}
