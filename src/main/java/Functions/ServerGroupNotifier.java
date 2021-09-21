package Functions;

import Functions.Configuration.ServerGroupNotifierConfiguration;
import Interfaces.FunctionInterface;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerGroupNotifier extends ConfigurationReader implements FunctionInterface {

    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(ServerGroupNotifier.class);
        final ServerGroupNotifierConfiguration conf = getConfig(ServerGroupNotifierConfiguration.class, path);

        ts3Api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {
                List<ServerGroup> serverGroups = ts3Api.getServerGroupsByClientId(clientJoinEvent.getClientDatabaseId());

                for (ServerGroup serverGroup : serverGroups) {
                    if (serverGroup.getId() == conf.getServerGroupIdToNotify()) {
                        logger.info("Notifying client: " + clientJoinEvent.getClientNickname());
                        ts3Api.sendPrivateMessage(clientJoinEvent.getClientId(), conf.getMessage());
                    }
                }
            }
        });
    }
}
