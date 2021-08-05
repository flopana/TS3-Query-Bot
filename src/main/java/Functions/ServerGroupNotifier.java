package Functions;

import Functions.Configuration.ServerGroupNotifierConfiguration;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;

import java.util.List;

public class ServerGroupNotifier extends ConfigurationReader implements FunctionInterface {

    @Override
    public void register(TS3Api ts3Api, String path) {

        final ServerGroupNotifierConfiguration conf = getConfig(ServerGroupNotifierConfiguration.class, path);

        ts3Api.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent textMessageEvent) {

            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {
                List<ServerGroup> serverGroups = ts3Api.getServerGroupsByClientId(clientJoinEvent.getClientDatabaseId());

                for (ServerGroup serverGroup : serverGroups){
                    if (serverGroup.getId() == conf.getServerGroupIdToNotify()){
                        ts3Api.sendPrivateMessage(clientJoinEvent.getClientId(), conf.getMessage());
                    }
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {

            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

            }

            @Override
            public void onClientMoved(ClientMovedEvent clientMovedEvent) {

            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

            }
        });
    }
}
