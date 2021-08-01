import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final BotConfiguration botConfiguration = BotConfiguration.loadAndGetConfig();

        final TS3Config ts3Config = new TS3Config();
        ts3Config.setHost(botConfiguration.getServerIp());

        final TS3Query ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();

        final TS3Api ts3Api = ts3Query.getApi();

        final CommandInvoker commandInvoker = new CommandInvoker(ts3Api, botConfiguration);

        ts3Api.login(botConfiguration.getQueryUsername(), botConfiguration.getQueryPassword());
        ts3Api.selectVirtualServerById(botConfiguration.getVirtualServerId());
        ts3Api.setNickname(botConfiguration.getBotName());

        //Only for development
//        ts3Api.sendPrivateMessage(100, "hi");
        ts3Api.sendPrivateMessage(91, "hi");
        List<Client> clients = ts3Api.getClients();

        ts3Api.registerAllEvents();
        ts3Api.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent textMessageEvent) {
                commandInvoker.invokeCommand(textMessageEvent);
            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {

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

        //Shutdown hook
        var shutdownListener = new Thread(() -> {
            byte[] array = new byte[32]; // length is bounded by 7
            new Random().nextBytes(array);
            String generatedString = new String(array, StandardCharsets.UTF_8);

            System.out.println("Shutting down.");
            ts3Api.setNickname(generatedString);
            ts3Query.exit();
        });
        Runtime.getRuntime().addShutdownHook(shutdownListener);
    }
}
