package Functions;

import Functions.Configuration.ChannelWatcherConfiguration;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class ChannelWatcher extends ConfigurationReader implements FunctionInterface {
    @Override
    public void register(TS3Api ts3Api, String path) {
        Logger logger = LoggerFactory.getLogger(ChannelWatcher.class);
        final ChannelWatcherConfiguration conf = getConfig(ChannelWatcherConfiguration.class, path);
        boolean validConf = true;
        String channelName = getChannelNameById(ts3Api, conf, logger);

        switch (conf.getClientJoinedMessageType()) {
            case "poke":
            case "text":
                break;
            default:
                logger.error("Invalid clientJoinMessageType: " + conf.getClientJoinedMessageType());
                validConf = false;
                break;
        }
        switch (conf.getServerGroupMessageType()) {
            case "poke":
            case "text":
                break;
            default:
                logger.error("Invalid serverGroupMessageType: " + conf.getServerGroupMessageType());
                validConf = false;
                break;
        }
        if (validConf) {
            ts3Api.addTS3Listeners(new TS3EventAdapter() {
                @Override
                public void onClientMoved(ClientMovedEvent e) {
                    if (e.getTargetChannelId() == conf.getChannelToWatch()) {
                        List<Client> clients = ts3Api.getClients();
                        for (Client client : clients) {
                            if (client.getId() == e.getClientId()) {
                                if (!isInGroups(client, conf.getIgnoreServerGroups())) {

                                    logger.info("Notifying: " + client.getId() + " with: " + conf.getClientJoinedMessage());

                                    if (Objects.equals(conf.getClientJoinedMessageType(), "poke")) {
                                        ts3Api.pokeClient(client.getId(), conf.getClientJoinedMessage());
                                    } else {
                                        ts3Api.sendPrivateMessage(client.getId(), conf.getClientJoinedMessage());
                                    }
                                }
                                for (Client client2 : clients) {
                                    if (isInGroups(client2, conf.getServerGroupsToNotify())) {
                                        String serverGroupMessage = "Client: " + client.getNickname() + " joined channel: " + channelName;
                                        logger.info("Notifying: " + client2.getId() + " with: " + serverGroupMessage);
                                        if (Objects.equals(conf.getServerGroupMessageType(), "poke")) {
                                            ts3Api.pokeClient(client2.getId(), serverGroupMessage);
                                        } else {
                                            ts3Api.sendPrivateMessage(client2.getId(), serverGroupMessage);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            });
        } else {
            logger.error("Didn't register function: " + ChannelWatcher.class + " Config path: " + path);
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

    public String getChannelNameById(TS3Api ts3Api, ChannelWatcherConfiguration conf, Logger logger) {
        String channelName = "";
        for (Channel channel : ts3Api.getChannels()){
            if (channel.getId() == conf.getChannelToWatch()){
                channelName = channel.getName();
                break;
            }
        }
        if (channelName.equals("")){
            logger.error("Couldn't find channelId: "+conf.getChannelToWatch());
        }

        return channelName;
    }
}
