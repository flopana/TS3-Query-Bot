package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetNicknameCommand implements ICommand {
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        Logger logger = LoggerFactory.getLogger(SetNicknameCommand.class);

        String nickname = e.getMessage().substring(13);
        logger.info("Setting nickname to '" + nickname + "' this action is not permanent.");
        ts3Api.setNickname(nickname);
    }
}
