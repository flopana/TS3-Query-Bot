import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        final BotConfiguration botConfiguration = BotConfiguration.loadAndGetConfig();

        final TS3Config ts3Config = new TS3Config();
        ts3Config.setHost(botConfiguration.getServerIp());

        logger.info("Connecting to Server");
        final TS3Query ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();

        final TS3Api ts3Api = ts3Query.getApi();

        ts3Api.login(botConfiguration.getQueryUsername(), botConfiguration.getQueryPassword());
        ts3Api.selectVirtualServerById(botConfiguration.getVirtualServerId());

        /*
         * For some reason the Nickname sometimes is still on some table in TS3 and thus the bot is unable to set
         * its nickname. The bot will then set a random name and tries again to set its configured name.
         */
        try {
            ts3Api.setNickname(botConfiguration.getBotName());
        } catch (TS3CommandFailedException e) {
            logger.info("Bot couldn't set the Nickname " + botConfiguration.getBotName());
            byte[] array = new byte[32];
            new Random().nextBytes(array);
            String generatedString = new String(array, StandardCharsets.US_ASCII);
            logger.info("Setting Nickname to a random string and then back to the configured one");
            ts3Api.setNickname(generatedString);
            ts3Api.setNickname(botConfiguration.getBotName());
        }

        logger.info("Registering commands");
        final CommandInvoker commandInvoker = new CommandInvoker(ts3Api, botConfiguration);

        logger.info("Registering functions");
        final FunctionInvoker functionInvoker= new FunctionInvoker(ts3Api, botConfiguration);
        functionInvoker.registerFunctions();

        //Only for development
//        ts3Api.sendPrivateMessage(140, "hi);
        ts3Api.sendPrivateMessage(92, "hi");
//        ts3Api.moveClient(140, 57);
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
            logger.info("Shutting Down");
            ts3Query.exit();
        });
        Runtime.getRuntime().addShutdownHook(shutdownListener);
    }
}
