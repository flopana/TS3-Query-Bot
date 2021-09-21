package Functions;

import Functions.Configuration.IdleCheckConfiguration;
import Interfaces.FunctionInterface;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IdleCheck extends ConfigurationReader implements FunctionInterface {
    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(IdleCheck.class);
        final IdleCheckConfiguration conf = getConfig(IdleCheckConfiguration.class, path);

        Thread idleCheckDaemon = new Thread() {
            @Override
            public void run() {
                while (true) {
                    List<Client> clients = ts3Api.getClients();
                    for (Client client : clients) {
                        if (isInChannels(client, conf.getChannelsToIgnore())) {
                            continue;
                        }
                        if (isInGroups(client, conf.getServerGroupsToIgnore())) {
                            continue;
                        }
                        if (client.getIdleTime() >= conf.getIdleTimeoutMillis()) {
                            logger.info("Moving Client: "+client.getNickname()+" to Channel: "+ts3Api.getChannelInfo(conf.getChannelToMoveTo()).getName());
                            ts3Api.moveClient(client.getId(), conf.getChannelToMoveTo());
                            ts3Api.sendPrivateMessage(client.getId(), "Hey " + client.getNickname() + " you got moved for being AFK");
                        }
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public boolean isInGroups(Client client, int[] serverGroupIds) {
                for (int serverGroupId : serverGroupIds) {
                    if (client.isInServerGroup(serverGroupId)) {
                        return true;
                    }
                }
                return false;
            }

            public boolean isInChannels(Client client, int[] channelIds) {
                for (int channelId : channelIds) {
                    if (client.getChannelId() == channelId) {
                        return true;
                    }
                }
                return false;
            }
        };

        idleCheckDaemon.setDaemon(true);
        logger.info("Starting IdleCheck Daemon thread");
        idleCheckDaemon.start();
    }
}
